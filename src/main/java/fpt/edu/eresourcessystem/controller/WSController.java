package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.model.Notification;
import fpt.edu.eresourcessystem.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class WSController {

    private final SimpMessagingTemplate messagingTemplate;


//    @MessageMapping("/sendNotification")
//    @SendToUser("/topic/private-messages")

}
