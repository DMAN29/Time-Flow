package com.app.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.exception.OrderException;
import com.app.exception.UserException;
import com.app.model.Operation;
import com.app.model.Order;
import com.app.model.Role;
import com.app.model.TimeStudy;
import com.app.model.User;
import com.app.repo.OrderRepo;
import com.app.request.AllowanceRequest;
import com.app.request.LaneRequest;
import com.app.request.LapsCountRequest;

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
//	public Order createOrder(Order order,MultipartFile file,String jwt) throws OrderException, UserException, IOException {
//		String email = userService.findUserProfileByJwt(jwt).getEmail();
//		if(orderRepo.findByStyleNo(order.getStyleNo()).isEmpty()){
//			order.setCreatedAt(LocalDateTime.now());
//			order.setCreatedBy(email);
//			Order savedOrder = orderRepo.save(order);
//			return updateOperations(file, savedOrder.getStyleNo());
//		}
//		throw new OrderException("Order already exist with Style No: " +order.getStyleNo());
//	}

	public Order createOrder(Order order,MultipartFile file,String jwt) throws OrderException, UserException, IOException {
		String email = userService.findUserProfileByJwt(jwt).getEmail();
		if(orderRepo.findByStyleNo(order.getStyleNo()).isEmpty()){
			order.setCreatedAt(LocalDateTime.now());
			order.setCreatedBy(email);
//			Order savedOrder = orderRepo.save(order);
			return updateOperations(file, order);
		}
		throw new OrderException("Order already exist with Style No: " +order.getStyleNo());
	}
	public List<Order> getAllorder() {
		return orderRepo.findAll();
	}

	public Order getOrderByStyleNo(String styleNo) throws OrderException {
		Optional<Order> order = orderRepo.findByStyleNo(styleNo);
		if(order.isEmpty()) {
			throw new OrderException("Order not exist with Style No: "+styleNo );
		}
		return order.get();
	}

	public Order getOrderByItemNo(String itemNo) throws OrderException {
		Optional<Order> order = orderRepo.findByItemNo(itemNo);
		if(order.isEmpty()) {
			throw new OrderException("Order not exist with Item No: "+itemNo);
		}
		return order.get();
	}

	

	public Order updateOperations(MultipartFile file, Order order) throws OrderException, IOException {
	    List<Operation> operations = new ArrayList<>();
	    InputStream inputStream = file.getInputStream();
	    Workbook workbook = new XSSFWorkbook(inputStream);
	    Sheet sheet = workbook.getSheetAt(0); // Read the first sheet

	    String sectionCellValue = "";
	    int index = 0;
	    double totalSam = 0;
	    double totalAllocation = 0;
	    double totalRequired = 0;
	    int totalRows = sheet.getPhysicalNumberOfRows();
	    int designedOutput = Integer.MAX_VALUE;
	    boolean nextSafe = false;
	    for (int i = 3; i < totalRows - 1; i++) { // Start from row index 3 to skip headers
	        Row row = sheet.getRow(i);
	        Row next = sheet.getRow(i + 1);
	        if (row == null) {
	            break; // Stop when encountering an empty row
	        }

	        Cell firstCell = row.getCell(0);
	        if (firstCell == null || firstCell.getCellType() == CellType.BLANK) {
	            break; // Stop when an empty row is encountered
	        }

	        Operation operation = new Operation();
	        operation.setId(++index);

	        Cell operationNameCell = row.getCell(1); // Column B
	        Cell sectionCell = row.getCell(2);       // Column C
	        Cell samCell = row.getCell(3);           // Column D
	        Cell machineTypeCell = row.getCell(4);   // Column E

	        Cell nextSection = next.getCell(2);
	        String nextSectionValue = "";
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

	        // **Calculate Required with precision (rounded to 2 decimal places)**
	        double nextSamValue = nextSam.getNumericCellValue();
	        double required = roundToTwo((order.getTarget() * operation.getSam()) / (480*0.01*order.getEfficiency()));
	        double nextRequired = roundToTwo((order.getTarget() * nextSamValue) / (480*0.01*order.getEfficiency()));
	        operation.setRequired(required);

	        // **Compute Allocated (Round up to nearest 0.5)**
	        double allocated = Math.ceil(required * 2) / 2;
	        double nextAllocated = Math.ceil(nextRequired * 2) / 2;
	        operation.setAllocated(allocated);

	        // **Set Target Calculation**
//	        if (operation.getSam() > 0) {
//	            operation.setTarget((int)Math.round((480 * allocated * 0.01 * order.getEfficiency()) / operation.getSam()));
//	        }
	        if (nextSection != null && !nextSection.getStringCellValue().trim().isEmpty()) {
	            nextSectionValue = nextSection.getStringCellValue().trim();
	        }
	        nextSectionValue=sectionCellValue;
	        if (!nextSafe && nextSectionValue.equals(sectionCellValue) && machineTypeCell.getStringCellValue().trim().equals(nextMachineType.getStringCellValue().trim()) && allocated % 1 == 0.5 && nextAllocated % 1 == 0.5) {
	            nextSafe = true;
	        } else {
	            if(!nextSafe && allocated%1==0.5) {
	            	allocated+=0.5;
	            }
	            nextSafe = false;
	        }
	        operation.setAllocated(allocated);
	        if (operation.getSam() > 0) {
	            operation.setTarget((int)Math.round((480 * allocated * 0.01 * order.getEfficiency()) / operation.getSam()));
	        }
	        totalSam += operation.getSam();
	        totalRequired += required;
	        totalAllocation += operation.getAllocated();
	        designedOutput = Math.min(operation.getTarget(), designedOutput);
	        operations.add(operation);
	    }

	    workbook.close();

	    // **Set Order Details**
	    order.setTotalAllocation(totalAllocation);
	    order.setTotalSam(roundToTwo(totalSam));
	    order.setTotalRequired(roundToTwo(totalRequired));
	    order.setDesignOutput(designedOutput);
	    if (totalAllocation > 0) {
	        order.setLineDesign((int) Math.round((order.getDesignOutput() * totalSam) / (totalAllocation * 480) * 100.0));
	    } else {
	        order.setLineDesign(0);
	    }

	    order.setOperations(operations);
	    return orderRepo.save(order);
	}

	// **Utility function for rounding to 2 decimal places**
	private double roundToTwo(double value) {
	    return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}
/// update the allowance then update the time study table too

	public String updateAllowance(AllowanceRequest allowance) throws OrderException {
		Order order = getOrderByStyleNo(allowance.getStyleNo());
		order.setAllowance(allowance.getAllowance());
		orderRepo.save(order);

		// ✅ Update TimeStudies for the same styleNo
		List<TimeStudy> allStudies = timeStudyService.getStudyByStyleNo(allowance.getStyleNo());
		for (TimeStudy study : allStudies) {
			timeStudyService.recalculateStudy(study); // custom method
		}

		return "Allowance set to " + allowance.getAllowance() + "%";
	}

	public String updateLane(LaneRequest lane) throws OrderException {
		Order order = getOrderByStyleNo(lane.getStyleNo());
		order.setLane(lane.getLane());
		orderRepo.save(order);
		return "Lane set to Lane no, :"+lane.getLane();
	}
	
	public Integer getAllowance(String styleNo) throws OrderException {
		return getOrderByStyleNo(styleNo).getAllowance();
	}
	
	public Integer getLane(String styleNo) throws OrderException {
		return getOrderByStyleNo(styleNo).getLane();
	}
	public String updateLapsCount(LapsCountRequest lap) throws OrderException {
		// TODO Auto-generated method stub
		Order order = getOrderByStyleNo(lap.getStyleNo());
		order.setNoOfLaps(lap.getNoOfLaps());
		orderRepo.save(order);
		return "Laps count set to "+lap.getNoOfLaps();
	}
	public void deleteOrder(String styleNo, String token) throws UserException, OrderException {
		List<Role> roles = userService.findUserProfileByJwt(token).getRole();
		if(roles.contains(Role.ROLE_ADMIN)) {
			orderRepo.deleteByStyleNo(styleNo);
			return;
		}
		throw new OrderException("Only Admin can delete Order");
	}


	

	
}
