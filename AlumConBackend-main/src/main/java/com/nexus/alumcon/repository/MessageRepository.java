package com.nexus.alumcon.repository;

import com.nexus.alumcon.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findAllByRoomId(String roomId);

    Page<Message> findAllByRoomId(String roomId, Pageable pageable);
}
