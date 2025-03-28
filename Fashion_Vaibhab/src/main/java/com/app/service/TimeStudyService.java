package com.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.exception.OrderException;
import com.app.exception.TimeStudyException;
import com.app.model.TimeStudy;
import com.app.repo.TimeStudyRepo;
import com.app.request.LapsRequest;
import com.app.request.OperatorRequest;
import com.app.request.RemarkRequest;

@Service
public class TimeStudyService {

    @Autowired
    private TimeStudyRepo timeStudyRepo;

    @Autowired
    private OrderService orderService;

    public TimeStudy storeStudy(TimeStudy study) throws TimeStudyException, OrderException {
        if (timeStudyRepo.existsByStyleNoAndOperatorId(study.getStyleNo(), study.getOperatorId())) {
            throw new TimeStudyException("Time Study already exists for Style No: " + study.getStyleNo() + " and Operator Id: " + study.getOperatorId());
        }
        return saveStudy(study);
    }


    public List<TimeStudy> getAllStudy() {
        return timeStudyRepo.findAll();
    }

    public TimeStudy getStudyByOperatorId(OperatorRequest operator) throws TimeStudyException {
        return timeStudyRepo.findByStyleNoAndOperatorId(operator.getStyleNo(),operator.getOperatorId())
                .orElseThrow(() -> new TimeStudyException("Time Study not found for Operator Id: " + operator.getOperatorId() +" and Style no. "+operator.getStyleNo() ));
        }

//    public List<TimeStudy> getStudyByOperatorId(String id) throws TimeStudyException {
//        List<TimeStudy> studies = timeStudyRepo.findByOperatorId(id);
//        if (studies.isEmpty()) {
//            throw new TimeStudyException("Time Study not found for Operator Id: " + id);
//        }
//        return studies;
//    }

    public TimeStudy updateLap(LapsRequest laps) throws TimeStudyException, OrderException {
        TimeStudy study = getStudyByOperatorId(laps.getOperator());
        study.setLapsMS(laps.getLapsMS());
        return saveStudy(study);
    }

    public String updateRemarks(RemarkRequest remarks) throws TimeStudyException {
        TimeStudy study = getStudyByOperatorId(remarks.getOperator());
        study.setRemarks(remarks.getRemarks());
        timeStudyRepo.save(study);
        return "Remarks successfully added for Operator Id: " + remarks.getOperator().getOperatorId() + " in Order Style no "+remarks.getOperator().getStyleNo();
    }

    private TimeStudy saveStudy(TimeStudy study) throws OrderException {
        long avgMillis = calculateAverageTime(study.getLapsMS());
        int allowancePercentage = orderService.getAllowance(study.getStyleNo());
        long allowanceMillis = avgMillis + (allowancePercentage * avgMillis) / 100;

        study.setLaps(formatLaps(study.getLapsMS()));
        study.setAvgTime(formatDuration(avgMillis));
        study.setAllowanceTime(formatDuration(allowanceMillis));
        study.setCapacityPH((int) ((3600 * 1000) / allowanceMillis));
        study.setCapacityPD((int) ((3600 * 8 * 1000) / allowanceMillis));

        return timeStudyRepo.save(study);
    }

    private List<String> formatLaps(List<Long> lapsMS) {
        return lapsMS.stream().map(this::formatDuration).collect(Collectors.toList());
    }

    private String formatDuration(long millis) {
        return String.format("%02d:%02d:%02d", (millis / 60000) % 60, (millis / 1000) % 60, (millis % 1000) / 10);
    }

    private long calculateAverageTime(List<Long> lapsMS) {
        return (lapsMS == null || lapsMS.isEmpty()) ? 0 : lapsMS.stream().mapToLong(Long::longValue).sum() / lapsMS.size();
    }
}
