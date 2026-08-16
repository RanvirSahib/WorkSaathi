package com.worksaathi.websocket;

import com.worksaathi.entity.Conversation;
import com.worksaathi.entity.Message;
import com.worksaathi.repository.ConversationRepository;
import com.worksaathi.repository.MessageRepository;
import com.worksaathi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;

    @MessageMapping("/chat/{conversationId}/send")
    @SendTo("/topic/chat/{conversationId}")
    public Message sendMessage(
            @DestinationVariable Long conversationId,
            String messageContent) {

        // Create and save message
        Message message = new Message();
        message.setConversation(conversationRepository.findById(conversationId).orElse(null));
        
        // In a real implementation, you'd get the sender from the security context
        // For now, this is a placeholder
        // message.setSender(currentUser);
        
        message.setMessage(messageContent);
        message.setIsRead(false);
        message.setCreatedAt(LocalDateTime.now());

        return messageRepository.save(message);
    }

    @MessageMapping("/chat/{conversationId}/read")
    @SendTo("/topic/chat/{conversationId}")
    public String markAsRead(@DestinationVariable Long conversationId) {
        // Mark messages as read for the conversation
        // This would be implemented with proper read receipt logic
        
        return "Messages marked as read";
    }
}
