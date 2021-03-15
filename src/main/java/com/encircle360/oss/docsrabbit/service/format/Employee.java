package com.encircle360.oss.docsrabbit.service.format;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Employee {
    private String name;
    private int age;
    private Double payment;
    private Double bonus;
    private Date birthDate;
    private Employee superior;
}
