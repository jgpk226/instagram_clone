package com.instagram.instagram.modules.Post;

import com.instagram.instagram.modules.Account.Account;
import com.instagram.instagram.modules.Account.AccountRepository;
import com.instagram.instagram.modules.Account.Follow;
import com.instagram.instagram.modules.Account.FollowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class PostService {

    private final FollowRepository followRepository;

    private final PostRepository postRepository;

    private final AccountRepository accountRepository;


    public void newPost(Account account, NewPostForm newPostForm) {
        Post post = Post.builder()
                .image(newPostForm.getImage())
                .content(newPostForm.getContent())
                .writer(account)
                .savedDate(LocalDateTime.now())
                .build();

//        // 이메일 확인을 거쳐야만 활동 가능
//        if(account.isEmailChecked()) {
//            postRepository.save(post);
//        } else {
//            throw new RuntimeException();
//        }

        postRepository.save(post);
        Account newAccount = accountRepository.findByUsername(account.getUsername());
        newAccount.getPostList().add(post);
        accountRepository.save(newAccount);
    }

    public Page<Post> findByAccount(Account account, Pageable pageable) {
        Set<Follow> followSet =  followRepository.findAllByFollower(account);
        Set<Account> accountSet = new HashSet<>();
        for(Follow follow : followSet) {
            accountSet.add(follow.getFollowing());
        }

        // querydsl 로 페이징 처리
        return postRepository.findByAccount(account, accountSet, pageable);
    }

    public boolean saveOrDeleteLike(Long postId, Long accountId) {
        Post post = postRepository.findById(postId).orElse(null);
        Account account = accountRepository.findById(accountId).orElse(null);

        Set<Account> accountSet = post.getLikedBy();
        if(!accountSet.contains(account)) {
            accountSet.add(account);
            return true;
        } else {
            accountSet.remove(account);
            return false;
        }

    }
}
