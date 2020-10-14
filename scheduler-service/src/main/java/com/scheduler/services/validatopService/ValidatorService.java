package com.scheduler.services.validatopService;

import com.scheduler.dto.AddPlanDto;


public interface ValidatorService {

    void checkService(AddPlanDto uuidChild);
}
