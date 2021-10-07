package com.instagram.instagram.modules.Account;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUsername(String username);

    Account findByEmail(String username);

    boolean existsByUsername(String userName);

    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = {"postList"}, type = EntityGraph.EntityGraphType.LOAD)
    Account findWithPostListById(Long id);
}
