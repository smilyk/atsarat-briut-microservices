package com.scheduler.services.planService;

import com.scheduler.dto.AddPlanDto;
import com.scheduler.dto.Response;
import com.scheduler.dto.ResponsePlanDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PlanService {
    Response addPlanDetails(AddPlanDto planDetails);

    ResponsePlanDTO getPlanDetaildByUuid(String uuidPlanDetails);

    List<ResponsePlanDTO> getPlanDetailsByChildUuid(String uuidChild, int page, int limit);

    Boolean deletePlanDetails(String uuidCPlanDetails);

    List<ResponsePlanDTO> getAllPlanDetails(int page, int limit);
}
