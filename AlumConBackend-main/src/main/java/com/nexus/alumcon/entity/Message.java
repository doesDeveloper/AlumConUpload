package com.nexus.alumcon.entity;

import com.nexus.alumcon.dto.chat.MessageDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "messages")
public class Message {
    @Id
    private String id;
    private String sender;
    private String content;
    private String roomId;
    private String timestamp;

    public Message(MessageDTO message) {
        this.setRoomId(message.getRoomId());
        this.setContent(message.getContent());
        this.setSender(message.getSender());
        this.setTimestamp(message.getTimestamp());
    }
}
