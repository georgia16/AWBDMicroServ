package com.example.awbdmicroservices.services.clients;

import com.example.awbdmicroservices.models.Discount;
import com.example.awbdmicroservices.models.Postpone;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "postpone")
public interface PostponeServiceProxy {
    @GetMapping(value = "/postpone", consumes = "application/json")
    Postpone findPostpone();
}