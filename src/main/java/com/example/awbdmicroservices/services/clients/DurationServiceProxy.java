package com.example.awbdmicroservices.services.clients;

import com.example.awbdmicroservices.models.Duration;
import com.example.awbdmicroservices.models.Postpone;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "duration")
public interface DurationServiceProxy {
    @GetMapping(value = "/duration", consumes = "application/json")
    Duration findDuration();
}
