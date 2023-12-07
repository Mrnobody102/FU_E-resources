package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.dto.NotificationDto;
import fpt.edu.eresourcessystem.dto.Response.NotificationResponseDto;
import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.model.Document;
import fpt.edu.eresourcessystem.model.Notification;
import fpt.edu.eresourcessystem.service.AccountService;
import fpt.edu.eresourcessystem.service.NotificationService;
import fpt.edu.eresourcessystem.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final QuestionService questionService;

    @MessageMapping("/private")
    @SendToUser("/notifications/private")
    public NotificationResponseDto sendMessage(@RequestBody final NotificationDto notificationDto,
                                               final Principal principal) {


        notificationDto.setFrom(principal.getName());
        Document document = questionService.findById(notificationDto.getQuestionId()).getDocumentId();
        notificationDto.setDocument(document);
        notificationDto.setTo(document.getCreatedBy());
        notificationDto.setLink("/lecturer/documents/"+document.getId());
        Notification notification = notificationService.addNotification(notificationDto);
        NotificationResponseDto notificationResponseDto = new NotificationResponseDto(
            notification
        );
        System.out.println(notificationResponseDto);
        return notificationResponseDto;
    }
}
