package com.gogo.psy.user.pojo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.Data;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Integer stock;

    @Version
    private Integer version;
}
