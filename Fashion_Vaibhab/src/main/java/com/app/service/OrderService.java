package com.app.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.exception.OrderException;
import com.app.exception.UserException;
import com.app.model.*;
import com.app.repo.OrderRepo;
import com.app.request.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    @Lazy
    private TimeStudyService timeStudyService;

    @Autowired
    private UserService userService;

    public Order createOrder(Order order, MultipartFile file, String jwt)
            throws OrderException, UserException, IOException {
        User user = userService.findUserProfileByJwt(jwt);
        if (orderRepo.findByStyleNo(order.getStyleNo()).isEmpty()) {
            order.setCreatedAt(LocalDateTime.now());
            order.setCreatedBy(user.getEmail());
            order.setCompany(user.getCompany());
            return updateOperations(file, order);
        }
        throw new OrderException("Order already exist with Style No: " + order.getStyleNo());
    }

    public List<Order> getAllorder(String token) throws UserException {
        User user = userService.findUserProfileByJwt(token);
        if (user.getRole().contains(Role.ROLE_HEAD)) {
            return orderRepo.findAll();
        }
        return orderRepo.findByCompany(user.getCompany());
    }

    public Order getOrderByStyleNo(String styleNo) throws OrderException {
        return orderRepo.findByStyleNo(styleNo).orElseThrow(() ->
                new OrderException("Order not exist with Style No: " + styleNo));
    }

    public Order getOrderByItemNo(String itemNo) throws OrderException {
        return orderRepo.findByItemNo(itemNo).orElseThrow(() ->
                new OrderException("Order not exist with Item No: " + itemNo));
    }

    public Order updateOperations(MultipartFile file, Order order) throws OrderException, IOException {
        List<Operation> operations = new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        String sectionCellValue = "";
        int index = 0;
        for (int i = 3; i < sheet.getPhysicalNumberOfRows() - 1; i++) {
            Row row = sheet.getRow(i);
            Row next = sheet.getRow(i + 1);
            if (row == null || row.getCell(0) == null || row.getCell(0).getCellType() == CellType.BLANK) {
                break;
            }

            Operation operation = new Operation();
            operation.setId(++index);

            Cell operationNameCell = row.getCell(1);
            Cell sectionCell = row.getCell(2);
            Cell samCell = row.getCell(3);
            Cell machineTypeCell = row.getCell(4);
            Cell nextSection = next.getCell(2);
            Cell nextSam = next.getCell(3);
            Cell nextMachineType = next.getCell(4);

            if (operationNameCell != null) {
                operation.setOperationName(operationNameCell.getStringCellValue().trim());
            }
            if (sectionCell != null && !sectionCell.getStringCellValue().trim().isEmpty()) {
                sectionCellValue = sectionCell.getStringCellValue().trim();
            }
            operation.setSection(sectionCellValue);

            if (samCell != null) {
                operation.setSam(samCell.getNumericCellValue());
            }
            if (machineTypeCell != null) {
                operation.setMachineType(machineTypeCell.getStringCellValue().trim());
            }

            operations.add(operation);
        }

        workbook.close();
        order.setOperations(operations);
        return recalculateOrderDetailsAndSave(order);
    }

    public String updateAllowance(AllowanceRequest allowance) throws OrderException {
        Order order = getOrderByStyleNo(allowance.getStyleNo());
        order.setAllowance(allowance.getAllowance());
        orderRepo.save(order);

        List<TimeStudy> allStudies = timeStudyService.getStudyByStyleNo(allowance.getStyleNo());
        for (TimeStudy study : allStudies) {
            timeStudyService.recalculateStudy(study);
        }
        return "Allowance set to " + allowance.getAllowance() + "%";
    }

    public String updateLane(LaneRequest lane) throws OrderException {
        Order order = getOrderByStyleNo(lane.getStyleNo());
        order.setLane(lane.getLane());
        orderRepo.save(order);
        return "Lane set to Lane no, :" + lane.getLane();
    }

    public Integer getAllowance(String styleNo) throws OrderException {
        return getOrderByStyleNo(styleNo).getAllowance();
    }

    public Integer getLane(String styleNo) throws OrderException {
        return getOrderByStyleNo(styleNo).getLane();
    }

    public String updateLapsCount(LapsCountRequest lap) throws OrderException {
        Order order = getOrderByStyleNo(lap.getStyleNo());
        order.setNoOfLaps(lap.getNoOfLaps());
        orderRepo.save(order);
        return "Laps count set to " + lap.getNoOfLaps();
    }

    public void deleteOrder(String styleNo, String token) throws UserException, OrderException {
        List<Role> roles = userService.findUserProfileByJwt(token).getRole();
        if (!roles.contains(Role.ROLE_ADMIN)) {
            throw new OrderException("Only Admin can delete Order");
        }
        timeStudyService.deleteByStyleNo(styleNo);
        orderRepo.deleteByStyleNo(styleNo);
    }

    public void updateTarget(TargetRequest request, String token) throws UserException, OrderException {
        User user = userService.findUserProfileByJwt(token);
        Order order = getOrderByStyleNo(request.getStyleNo());
        if (!order.getCreatedBy().equals(user.getEmail())) {
            throw new OrderException("You are not authorized to update this order.");
        }
        order.setTarget(request.getTarget());
        recalculateOrderDetailsAndSave(order);
    }

    public void updateEfficiency(EfficiencyRequest request, String token) throws UserException, OrderException {
        User user = userService.findUserProfileByJwt(token);
        Order order = getOrderByStyleNo(request.getStyleNo());
        if (!order.getCreatedBy().equals(user.getEmail())) {
            throw new OrderException("You are not authorized to update this order.");
        }
        order.setEfficiency(request.getEfficiency());
        recalculateOrderDetailsAndSave(order);
    }

    private Order recalculateOrderDetailsAndSave(Order order) {
        List<Operation> operations = order.getOperations();
        double totalSam = 0, totalRequired = 0, totalAllocation = 0;
        int designOutput = Integer.MAX_VALUE;
        boolean nextSafe = false;

        for (int i = 0; i < operations.size(); i++) {
            Operation op = operations.get(i);
            double required = roundToTwo((order.getTarget() * op.getSam()) / (480 * 0.01 * order.getEfficiency()));
            double allocated = Math.ceil(required * 2) / 2;

            if (i + 1 < operations.size()) {
                Operation next = operations.get(i + 1);
                double nextRequired = roundToTwo((order.getTarget() * next.getSam()) / (480 * 0.01 * order.getEfficiency()));
                double nextAllocated = Math.ceil(nextRequired * 2) / 2;

                if (!nextSafe && op.getSection().equals(next.getSection())
                        && op.getMachineType().equals(next.getMachineType())
                        && allocated % 1 == 0.5 && nextAllocated % 1 == 0.5) {
                    nextSafe = true;
                } else {
                    if (!nextSafe && allocated % 1 == 0.5) {
                        allocated += 0.5;
                    }
                    nextSafe = false;
                }
            }

            op.setRequired(required);
            op.setAllocated(allocated);
            op.setTarget((int) Math.round((480 * allocated * 0.01 * order.getEfficiency()) / op.getSam()));

            totalSam += op.getSam();
            totalRequired += required;
            totalAllocation += allocated;
            designOutput = Math.min(op.getTarget(), designOutput);
        }

        order.setTotalSam(roundToTwo(totalSam));
        order.setTotalRequired(roundToTwo(totalRequired));
        order.setTotalAllocation(totalAllocation);
        order.setDesignOutput(designOutput);

        if (totalAllocation > 0) {
            order.setLineDesign((int) Math.round((designOutput * totalSam) / (totalAllocation * 480) * 100.0));
        } else {
            order.setLineDesign(0);
        }

        return orderRepo.save(order);
    }

    private double roundToTwo(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
