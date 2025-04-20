package com.nexus.alumcon.repository;

import com.nexus.alumcon.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("{'$or':["
            + "{'username': {$regex: ?0, $options: 'i'}},"
            + "{'firstName': {$regex: ?0, $options: 'i'}},"
            + "{'lastName': {$regex: ?0, $options: 'i'}},"
            + "{'email': {$regex: ?0, $options: 'i'}}"
            + "]}")
    Page<User> searchUsers(String query, Pageable pageable);
}
