package com.devstack.multithreadbatchprocessor.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
    @Id
    private Long id;
    private String name;
    private String category;
    private double price;
    @Column(name = "isOfferApplied")
    private boolean isOfferApplied;
    @Column(name = "discountPercentage")
    private double discountPercentage;
    @Column(name = "priceAfterDiscount")
    private double priceAfterDiscount;
}
