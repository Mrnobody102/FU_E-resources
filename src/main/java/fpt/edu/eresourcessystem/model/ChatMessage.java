package fpt.edu.eresourcessystem.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {
    private String content;
    private String sender;
    private MessageType type;
    public enum MessageType {

        CHAT,
        JOIN,
        LEAVE
    }
}
