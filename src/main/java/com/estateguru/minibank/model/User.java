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
    @Column(nullable = false,unique = true)
    private String code;
    @Column(nullable = false,unique = true)
    private String userName;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.ORDINAL)
    private RoleType role;
    @Transient
    private Boolean loggedIn;
    @OneToMany(mappedBy = "user")
    private List<LimitationUserCurrency> limitationUserCurrency;
}
