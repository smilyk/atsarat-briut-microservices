package com.scheduler.controller;

import com.scheduler.dto.AddPlanDto;
import com.scheduler.dto.OperationStatusModel;
import com.scheduler.dto.Response;
import com.scheduler.dto.ResponsePlanDTO;
import com.scheduler.enums.RequestOperationName;
import com.scheduler.enums.RequestOperationStatus;
import com.scheduler.services.planService.PlanService;
import com.scheduler.services.shedulerService.Scheduler;
import com.scheduler.services.validatopService.ValidatorService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/plan/v1")
public class PlanController {

    private static final String NOT_FOUND_STRING = " Can`t find record for child with uuid: ";
    private String currentDate = LocalDateTime.now().toLocalDate().toString();
    ModelMapper modelMapper = new ModelMapper();


    @Autowired
    PlanService planService;
    @Autowired
    Scheduler scheduler;
    @Autowired
    ValidatorService validatorService;


    @PostMapping()
    public Response createPlanDetails(@Valid @RequestBody AddPlanDto planDetails) {

        validatorService.checkService(planDetails);
        return planService.addPlanDetails(planDetails);
    }

    @GetMapping(path = "/{uuidPlanDetails}")
    public Response getPlanDetailsByUuid(@PathVariable String uuidPlanDetails) {
        ResponsePlanDTO responsePlanDTO = planService.getPlanDetaildByUuid(uuidPlanDetails);
        if(responsePlanDTO == null){
            return new Response(NOT_FOUND_STRING + uuidPlanDetails,
                    HttpServletResponse.SC_NO_CONTENT, currentDate);
        }
        return new Response(responsePlanDTO, HttpServletResponse.SC_FOUND, currentDate);
    }

    @GetMapping(path = "plan/{uuidChild}")
    public Response getPlanDetailsByChildUuid(@RequestParam(value = "page", defaultValue = "0") int page,
                                              @RequestParam(value = "limit", defaultValue = "10") int limit,
                                              @PathVariable String uuidChild) {
        List<ResponsePlanDTO>  responsePlanDTO = planService.getPlanDetailsByChildUuid(uuidChild, page, limit);
        Type listType = new TypeToken<List<ResponsePlanDTO>>() {
        }.getType();
        List<ResponsePlanDTO> returnValue = new ModelMapper().map(responsePlanDTO, listType);
        if(responsePlanDTO == null){
            return new Response(NOT_FOUND_STRING + uuidChild,
                    HttpServletResponse.SC_NO_CONTENT, currentDate);
        }
        return new Response(responsePlanDTO, HttpServletResponse.SC_FOUND, currentDate);
    }

    @DeleteMapping(path = "/{uuidCPlanDetails}")
    public Response deletePlanDetails(@PathVariable String uuidCPlanDetails) {
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());
        Boolean deleted = planService.deletePlanDetails(uuidCPlanDetails);
        if(deleted){
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }else{
            returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
        }
        return new Response(returnValue, HttpServletResponse.SC_OK, currentDate);
    }

    @GetMapping()
    public Response getAllPlanDetails(@RequestParam(value = "page", defaultValue = "0") int page,
                                       @RequestParam(value = "limit", defaultValue = "10") int limit) {
        List<ResponsePlanDTO> childDetails = planService.getAllPlanDetails(page, limit);
        Type listType = new TypeToken<List<ResponsePlanDTO>>() {
        }.getType();
        List<ResponsePlanDTO> returnValue = new ModelMapper().map(childDetails, listType);
        return new Response(returnValue, HttpServletResponse.SC_FOUND, currentDate);
    }

    @GetMapping(path = "1")
    public String check() {
        scheduler.scheduleCleanNotConfirmedEmail();
        return "done";
    }

}
