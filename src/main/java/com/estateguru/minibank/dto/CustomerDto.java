package com.estateguru.minibank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private String name;
    private String family;
    private String code;
    private Date birthDate;
    private Date createDate;
    private Date updateDate;
}
