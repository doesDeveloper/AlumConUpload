package com.nexus.alumcon.dto.chat;
import com.nexus.alumcon.entity.Message;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MessageDTO {
    private String sender;
    private String content;
    private String roomId;
    private String timestamp;
    public MessageDTO(Message message){
        this.setRoomId(message.getRoomId());
        this.setContent(message.getContent());
        this.setSender(message.getSender());
        this.setTimestamp(message.getTimestamp());
    }
}
