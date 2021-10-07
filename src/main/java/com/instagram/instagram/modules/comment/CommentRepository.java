package com.instagram.instagram.modules.comment;

import com.instagram.instagram.modules.Post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Set;

@Transactional
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Set<Comment> findAllByForPost(Post post);
}
