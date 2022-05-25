package com.example.awbdmicroservices.services.clients;

import com.example.awbdmicroservices.models.Discount;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "discount")
public interface DiscountServiceProxy {
    @GetMapping(value = "/discount", consumes = "application/json")
    Discount findDiscount();
}
