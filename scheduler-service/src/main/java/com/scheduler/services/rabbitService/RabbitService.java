package com.scheduler.services.rabbitService;

import com.scheduler.entity.PlanEntity;
import org.springframework.stereotype.Service;

@Service
public interface RabbitService {
    void sendMessageToServer(PlanEntity record);
}
