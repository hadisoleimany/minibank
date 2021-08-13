package com.estateguru.minibank.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String family;
    private String code;
    private Date birthDate;
    private Date createDate;
    private Date updateDate;
    @OneToMany(mappedBy = "customer" ,fetch = FetchType.LAZY)
    private Set<BankAccount> accounts;
}

