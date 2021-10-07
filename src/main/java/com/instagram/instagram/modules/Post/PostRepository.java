package com.instagram.instagram.modules.Post;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryExtension {
}
