package com.instagram.instagram.modules.Account.validator;

import com.instagram.instagram.modules.Account.AccountRepository;
import com.instagram.instagram.modules.Account.form.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final AccountRepository accountRepository;


    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUpForm signUpForm = (SignUpForm) target;

        if(accountRepository.findByEmail(signUpForm.getEmail()) != null) {
            errors.rejectValue("email", "invalid email", new Object[]{signUpForm.getEmail()}, "이미 사용중인 이메일입니다.");
        }

        if(accountRepository.findByUsername(signUpForm.getUserName()) != null) {
            errors.rejectValue("userName", "invalid userName", new Object[]{signUpForm.getUserName()}, "이미 사용중인 닉네임입니다.");
        }
    }
}
