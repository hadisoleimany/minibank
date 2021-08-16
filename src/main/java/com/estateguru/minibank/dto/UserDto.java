package com.estateguru.minibank.dto;

import com.estateguru.minibank.model.RoleType;
import com.estateguru.minibank.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    public UserDto(User user) {
        this.name = user.getName();
        this.code = user.getCode();
        this.userName = user.getUserName();
        this.password = user.getPassword();
        this.role = user.getRole();
    }

    private String name;
    private String code;
    private String userName;
    private String password;
    private RoleType role;
}
