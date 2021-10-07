package com.instagram.instagram.modules.Account;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Set;

@Transactional
public interface FollowRepository extends JpaRepository<Follow, Long> {
    Set<Follow> findAllByFollowing(Account account);

    Set<Follow> findAllByFollower(Account account);
}
