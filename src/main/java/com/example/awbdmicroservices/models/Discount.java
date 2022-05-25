package com.example.awbdmicroservices.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Discount {
    private String instanceId;
    private int month;
    private int year;
}
