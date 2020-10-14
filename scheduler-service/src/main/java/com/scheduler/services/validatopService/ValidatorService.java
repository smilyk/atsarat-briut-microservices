package com.scheduler.services.validatopService;

import com.scheduler.dto.AddPlanDto;
import org.springframework.stereotype.Service;

@Service
public interface ValidatorService {

    void checkService(AddPlanDto uuidChild);
}
