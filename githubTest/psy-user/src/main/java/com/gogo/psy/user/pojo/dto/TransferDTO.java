package com.gogo.psy.user.pojo.dto;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class TransferDTO {

    private String fromAccount;

    private String toAccount;

    private Integer amount;
}
