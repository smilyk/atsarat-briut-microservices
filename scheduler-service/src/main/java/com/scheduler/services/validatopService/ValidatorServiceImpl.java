package com.scheduler.services.validatopService;

import com.scheduler.dto.AddPlanDto;
import com.scheduler.enums.ErrorMessages;
import com.scheduler.enums.LoggerMessages;
import com.scheduler.enums.Services;
import com.scheduler.exception.ErrorMessage;
import com.scheduler.exception.SchedulerServiceException;
import com.scheduler.repository.PlanRepo;
import com.scheduler.services.planService.PlanServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
public class ValidatorServiceImpl implements ValidatorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorServiceImpl.class);
    @Autowired
    PlanRepo planRepo;
    @Override
    public void checkService(@Valid AddPlanDto planDto) {
        for (Services s : Services.values()) {
            if (!s.name().equals(planDto.getService())) {
                LOGGER.error(LoggerMessages.SERVICE + planDto.getService() + LoggerMessages.NOT_FOUND);
                throw new SchedulerServiceException(ErrorMessages.SERVICE + planDto.getService() +
                        ErrorMessages.NOT_FOUND);
            }
        }
    }
}
