package com.instagram.instagram.modules.Account;

import com.instagram.instagram.modules.Account.form.AccountEditForm;
import com.instagram.instagram.modules.Account.form.SignUpForm;
import com.instagram.instagram.modules.Post.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;

    private final FollowRepository followRepository;

    private final PasswordEncoder passwordEncoder;


    public Account signUp(SignUpForm signupForm) {
        Account account = Account.builder()
                .email(signupForm.getEmail())
                .realName(signupForm.getRealName())
                .username(signupForm.getUserName())
                .password(passwordEncoder.encode(signupForm.getPassword()))
                .build();

        Account newAccount = accountRepository.save(account);
        return newAccount;
    }

    public void login(Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new User(account), account.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(token);
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);

        if(account == null) {
            account = accountRepository.findByEmail(username);
        }

        if(account == null) {
            throw new UsernameNotFoundException(username);
        }

        return new User(account);
    }

    public int countFollower(Account account) {
        Set<Follow> follower = followRepository.findAllByFollowing(account);

        if(!follower.isEmpty()) {
            return follower.size();
        } else {
            return 0;
        }
    }

    public int countFollowing(Account account) {
        Set<Follow> following = followRepository.findAllByFollower(account);

        if(!following.isEmpty()) {
            return following.size();
        } else {
            return 0;
        }
    }

    public void editAccount(Account account, AccountEditForm accountEditForm) {
        Account newAccount = accountRepository.findByEmail(account.getEmail());

        // 이메일을 바꾸는 경우에는 인증상태 초기화
        if(!newAccount.getEmail().equals(accountEditForm.getEmail())) {
            newAccount.setEmailChecked(false);
        }

        newAccount.setRealName(accountEditForm.getRealName());
        newAccount.setUsername(accountEditForm.getUserName());
        newAccount.setDescription(accountEditForm.getDescription());
        newAccount.setEmail(accountEditForm.getEmail());

        accountRepository.save(newAccount);
        // Account이므로 로그인 token 값도 바꿔줘야함..!
        SecurityContextHolder.clearContext();
        login(newAccount);
    }

    public void changePassword(Account account, String newPassword) {
        Account newAccount = accountRepository.findByEmail(account.getEmail());
        newAccount.setPassword(passwordEncoder.encode(newPassword));

        SecurityContextHolder.clearContext();
        login(newAccount);
    }

    public Set<Post> getPostList(Account pageAccount) {
        Account account = accountRepository.findWithPostListById(pageAccount.getId());
        return account.getPostList();
    }
}
