package com.instagram.instagram.modules.Account.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class SignUpForm {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Length(min = 1, max = 10)
    private String realName;

    @NotBlank
    @Length(min = 3, max = 20)
    private String userName;

    @NotBlank
    @Length(min = 8, max = 20)
    private String password;
}
