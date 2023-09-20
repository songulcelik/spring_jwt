package com.tpe.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
//? 46****
@Data
public class LoginRequest {

    @NotBlank
    @NotNull
    private String userName;

    @NotBlank
    @NotNull
    private String password;
}//47 UserJwtController
