package com.instagram.instagram.modules.Account.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class AccountEditForm {

    @NotBlank
    @Length(min = 1, max = 10)
    private String realName;

    @NotBlank
    @Length(min = 3, max = 20)
    private String userName;

    private String description;

    @NotBlank
    @Email
    private String email;
}
