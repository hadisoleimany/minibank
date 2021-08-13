package com.estateguru.minibank.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String code;
    private boolean admin;
    private String username;
    private String password;
    @OneToMany(mappedBy = "user")
    private List<LimitationUserCurrency> limitationUserCurrency;
}
