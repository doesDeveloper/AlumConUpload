package com.nexus.alumcon.service;

import com.nexus.alumcon.dto.chat.MessageDTO;
import com.nexus.alumcon.entity.Message;
import com.nexus.alumcon.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public Message saveMessage(MessageDTO message) {
        return messageRepository.save(new Message(message));
    }

    public List<MessageDTO> getMessagesByRoom(String roomId) {
        List<Message> messages = messageRepository.findAllByRoomId(roomId);
        return messages.stream().map(MessageDTO::new).collect(Collectors.toList());
    }

    public List<MessageDTO> getMessagesByRoom(String roomId, int page, int size) {
        size = Math.min(size, 50);
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        List<Message> messages = messageRepository.findAllByRoomId(roomId, pageable).getContent();
        return messages.stream().map(MessageDTO::new).collect(Collectors.toList());
    }
}
