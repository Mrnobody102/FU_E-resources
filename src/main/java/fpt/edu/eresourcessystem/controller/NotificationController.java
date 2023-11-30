package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.model.ChatMessage;
import fpt.edu.eresourcessystem.model.Notification;
import fpt.edu.eresourcessystem.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final SimpMessageSendingOperations simpMessagingTemplate;
    private final AccountService accountService;

    private Account getLoggedInAccount() {
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(loggedInEmail);
        if (null == loggedInEmail || "anonymousUser".equals(loggedInEmail)) {
            return null;
        }
        Account loggedInAccount = accountService.findByEmail(loggedInEmail);
        if (null!= loggedInAccount) {
            return loggedInAccount;
        } else return null;
    }
//    @EventListener
//    public void handleSessionConnectEvent(SessionConnectEvent event) {
//        System.out.println("Session Connect Event");
//    }

//    @EventListener
//    public void handleSessionDisconnectEvent(SessionDisconnectEvent event) {
//        System.out.println("Session Disconnect Event");
//        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
//        if (sessionAttributes == null) {
//            return;
//        }
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Notification notification = new Notification(loggedInEmail, "admin@fpt.edu.com", loggedInEmail+" left", "");
//        simpMessagingTemplate.convertAndSend("/leave/messages", notification);
//    }

    @MessageMapping("/chat.addUser")
//    @SendTo("/student/chat")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("email", chatMessage.getSender());
        simpMessagingTemplate.convertAndSend("/student/chat", chatMessage);
        return chatMessage;
    }


    @MessageMapping("/chat.sendMessage")
//    @SendToUser("/student/chat")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("email", chatMessage.getSender());
        Notification notification = new Notification();
        // Gửi thông điệp đến người dùng có địa chỉ email là "thuongdthe150682@gmail.com"
        simpMessagingTemplate.convertAndSendToUser("thuongdthe150682@gmail.com", "/student/chat", chatMessage);
        return chatMessage;
    }

    @MessageMapping("/chat.addNotification")
//    @SendToUser("/student/chat")
    public Notification addNotification(@Payload ChatMessage chatMessage,
                        final Principal principal,
                        SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("email", chatMessage.getSender());
        // Gửi thông điệp đến người dùng có địa chỉ email là "thuongdthe150682@gmail.com"
        simpMessagingTemplate.convertAndSendToUser("thuongdthe150682@gmail.com", "/student/home", chatMessage);
        Notification notification = new Notification();
        notification.setFromAccount(principal.getName());
        return notification;
    }

    private boolean isMatchingUser(String userEmail, String targetEmail) {
        return userEmail.equals(targetEmail);
    }

    private boolean isUserAtHome(Authentication authentication) {
        // Kiểm tra xem người dùng có đang ở trang /home hay không
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"));
    }
}
