package com.instagram.instagram.modules.Account;

import com.instagram.instagram.modules.Account.form.AccountEditForm;
import com.instagram.instagram.modules.Account.form.PasswordForm;
import com.instagram.instagram.modules.Account.form.SignUpForm;
import com.instagram.instagram.modules.Account.validator.PasswordValidator;
import com.instagram.instagram.modules.Account.validator.SignUpFormValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
@Log4j2
public class AccountController {

    private final AccountRepository accountRepository;

    private final AccountService accountService;

    private final FollowService followService;

    private final SignUpFormValidator signUpFormValidator;

    private final PasswordValidator passwordValidator;

    private final PasswordEncoder passwordEncoder;


    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute("signUpForm", new SignUpForm());

        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(@Valid @ModelAttribute("signUpForm") SignUpForm signUpForm, Errors errors, Model model) {

        signUpFormValidator.validate(signUpForm, errors);

        if(errors.hasErrors()) {
            log.info(errors.getAllErrors());
            return "account/sign-up";
        } else {
            Account account = accountService.signUp(signUpForm);
            accountService.login(account);

            return "redirect:/";
        }
    }

    @GetMapping("/edit")
    public String editForm(@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account") Account account, Model model) {
        AccountEditForm accountEditForm = new AccountEditForm();
        accountEditForm.setRealName(account.getRealName());
        accountEditForm.setUserName(account.getUsername());
        accountEditForm.setDescription(account.getDescription());
        accountEditForm.setEmail(account.getEmail());

        model.addAttribute("account", account);
        model.addAttribute("accountEditForm", accountEditForm);

        return "account/edit";
    }

    @PostMapping("/edit")
    public String edit(@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account") Account account,
                       @Valid @ModelAttribute AccountEditForm accountEditForm, Errors errors, Model model) {
        // 현 닉네임과 바꾸려는 닉네임이 같으면 중복체크 필요 없음
        if(!account.getUsername().equals(accountEditForm.getUserName()) && accountRepository.existsByUsername(accountEditForm.getUserName())) {
            errors.rejectValue("userName", "existing value", "이미 사용중인 이름입니다.");
        }

        // 현 이메일과 바꾸려는 이메일이 같으면 중복체크 필요 없음
        if(!account.getEmail().equals(accountEditForm.getEmail()) && accountRepository.existsByEmail(accountEditForm.getEmail())) {
            errors.rejectValue("email", "existing value", "이미 사용중인 이메일입니다.");
        }

        if(errors.hasErrors()) {
            model.addAttribute("account", account);
            return "account/edit";
        } else {
            accountService.editAccount(account, accountEditForm);

            return "redirect:/account/edit";
        }
    }

    @GetMapping("/password/change")
    public String passwordChangeForm(@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account") Account account, Model model) {
        model.addAttribute("account", account);
        model.addAttribute("passwordForm", new PasswordForm());

        return "account/password-change";
    }

    @PostMapping("/password/change")
    public String passwordChange(@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account") Account account,
                                 @Valid @ModelAttribute PasswordForm passwordForm, Errors errors, Model model) {


        if(!passwordEncoder.matches(passwordForm.getLastPassword(), account.getPassword())) {
            errors.rejectValue("lastPassword", "wrong value", "이전 비밀번호가 올바르지 않습니다.");
        }

        passwordValidator.validate(passwordForm, errors);

        if(errors.hasErrors()) {
            model.addAttribute("account", account);
            return "account/password-change";
        } else {
            accountService.changePassword(account, passwordForm.getNewPassword());
            return "redirect:/account/password/change";
        }
    }

    @GetMapping("")
    public String follow(@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account") Account account,
                         @RequestParam Long from, @RequestParam Long to, @RequestParam("account") String pageUsername) {
        followService.follow(from, to);

        return "redirect:/" + pageUsername;
    }

    @GetMapping("/delete")
    public String deleteFollow(@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account") Account account,
                         @RequestParam Long from, @RequestParam Long to, @RequestParam("account") String pageUsername) {
        followService.deleteFollow(from, to);

        return "redirect:/" + pageUsername;
    }
}
