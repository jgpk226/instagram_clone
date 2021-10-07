package com.instagram.instagram.modules.Post;

import com.instagram.instagram.modules.Account.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.Set;

@Transactional
public interface PostRepositoryExtension {
    Page<Post> findByAccount(Account account, Set<Account> accountSet, Pageable pageable);
}
