package com.example.awbdmicroservices.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Postpone {
    private String instanceId;
    private int days;
    private int months;
}
