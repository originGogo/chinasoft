package com.gogo.psy.user.pojo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NonNull;

@Entity
@Data
public class UserAccount {

    @Id
    @GeneratedValue
    private Long id;

    private Long uid;

    private String bankAccount;

    private Integer balance;

}
