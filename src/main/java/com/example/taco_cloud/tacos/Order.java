package com.example.taco_cloud.tacos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class Order {

    private Long id;
    private Date placedAt;

    @NotBlank(message = "Name is required")
    private String deliveryName;
    @NotBlank(message = "Name is required")
    private String deliveryStreet;
    @NotBlank(message = "Name is required")
    private String deliveryCity;
    @NotBlank(message = "Name is required")
    private String deliveryState;
    @NotBlank(message = "Name is required")
    private String deliveryZip;
    @NotBlank(message = "Name is required")
    private String ccNumber;
    @NotBlank(message = "Name is required")
    private String ccExpiration;
    @NotBlank(message = "Name is required")
    private String ccCVV;
}
