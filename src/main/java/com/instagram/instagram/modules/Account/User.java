package com.instagram.instagram.modules.Account;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class User extends org.springframework.security.core.userdetails.User {

    public Account account;

    public User(Account account) {
        super(account.getUsername(), account.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.account = account;
    }

}
