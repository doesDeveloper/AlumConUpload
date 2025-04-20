package com.nexus.alumcon.repository;

import com.nexus.alumcon.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {
    Page<Post> findByUsername(String username, Pageable pageable);
}
