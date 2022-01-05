package com.example.taco_cloud.tacos;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "Taco_Order")
public class Order {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @ManyToMany(targetEntity = Taco.class)
    private List<Taco> tacos = new ArrayList<>();

    @PrePersist
    void placedAt() {
        this.placedAt = new Date();
    }

    public void addDesign(Taco design) {
        this.tacos.add(design);
    }
}
