package com.instagram.instagram.modules.comment;

import com.instagram.instagram.modules.Account.Account;
import com.instagram.instagram.modules.Post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    public void saveComment(Long postId, String conent, Account account) {
        Comment comment = Comment.builder()
                .comment(conent)
                .writer(account)
                .forPost(postRepository.findById(postId).get())
                .savedDate(LocalDateTime.now())
                .build();
        commentRepository.save(comment);
    }
}
