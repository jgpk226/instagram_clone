package com.instagram.instagram.modules.Account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowService {

    private final AccountRepository accountRepository;

    private final FollowRepository followRepository;


    public void follow(Long from, Long to) {
        Account follower = accountRepository.findById(from).orElse(null);
        Account following = accountRepository.findById(to).orElse(null);

        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .followingDate(LocalDateTime.now())
                .build();
        followRepository.save(follow);
    }

    public void deleteFollow(Long from, Long to) {
        Account follower = accountRepository.findById(from).orElse(null);
        Account following = accountRepository.findById(to).orElse(null);

        Set<Follow> follows = followRepository.findAllByFollower(follower);
        for(Follow follow : follows) {
            if(follow.getFollowing().equals(following)) {
                followRepository.delete(follow);
            }
        }
    }

    public boolean isFollowing(Account account, Account pageAccount) {
        Set<Follow> follows = followRepository.findAllByFollower(account);
        for(Follow follow : follows) {
            if(follow.getFollowing().equals(pageAccount)) {
                return true;
            }
        }
        return false;
    }

}
