package com.nexus.alumcon.controller;

import com.nexus.alumcon.dto.chat.MessageDTO;
import com.nexus.alumcon.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
public class ChatController {
    @Autowired
    private MessageService messageService;

    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/chat/{roomId}")
    public MessageDTO messageHandle(@DestinationVariable("roomId") String roomId, MessageDTO message) {
        message.setRoomId(roomId);
        message.setTimestamp(String.valueOf(System.currentTimeMillis()));
        System.out.println("Received: " + message);
        System.out.println(messageService.saveMessage(message));

        return message;
    }

    @CrossOrigin("*")
    @GetMapping("/chat/history/{roomId}")
    public List<MessageDTO> getChatHistory(@PathVariable("roomId") String roomId, @RequestParam(defaultValue = "0", name = "page") int page, @RequestParam(defaultValue = "50", name = "size") int size) {
        return messageService.getMessagesByRoom(roomId, page, size);
    }
}
