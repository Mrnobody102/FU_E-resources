package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.dto.NotificationDto;
import fpt.edu.eresourcessystem.dto.Response.NotificationResponseDto;
import fpt.edu.eresourcessystem.model.*;
import fpt.edu.eresourcessystem.service.*;
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
    private final AnswerService answerService;
    private final FeedbackService feedbackService;

    @MessageMapping("/private")
    @SendToUser("/notifications/private")
    public NotificationResponseDto sendMessage(@RequestBody final NotificationDto notificationDto,
                                               final Principal principal) {
        notificationDto.setFrom(principal.getName());
        if(notificationDto.getType().equals("1")) {
            Question question = questionService.findById(notificationDto.getQuestionId());
            notificationDto.setDocument(question.getDocumentId());
            notificationDto.setTo(question.getLecturer());
            notificationDto.setLink("/lecturer/documents/" + question.getDocumentId().getId() + "#question");
        }
        if (notificationDto.getType().equals("3")) {
            Answer answer = answerService.findById(notificationDto.getAnswerId());
            notificationDto.setDocument(answer.getDocumentId());
            notificationDto.setTo(answer.getDocumentId().getCreatedBy());
            notificationDto.setLink("/lecturer/documents/" + answer.getDocumentId().getId() + "#question");
        }
        Notification notification = notificationService.addNotification(notificationDto);
        NotificationResponseDto notificationResponseDto = new NotificationResponseDto(
            notification
        );
        return notificationResponseDto;
    }

    @MessageMapping("/reply")
    @SendToUser("/notifications/reply")
    public NotificationResponseDto sendReply(@RequestBody final NotificationDto notificationDto,
                                               final Principal principal) {
        notificationDto.setFrom(principal.getName());
        if (notificationDto.getType().equals("2")) {
            Answer answer = answerService.findById(notificationDto.getAnswerId());
            notificationDto.setDocument(answer.getDocumentId());
            // mail of lecturer
            notificationDto.setTo(answer.getQuestion().getCreatedBy());
            notificationDto.setLink("/student/documents/" + answer.getDocumentId().getId() + "#question");
        }
        Notification notification = notificationService.addNotification(notificationDto);
        NotificationResponseDto notificationResponseDto = new NotificationResponseDto(
                notification
        );
        return notificationResponseDto;
    }

    @MessageMapping("/feedback")
    @SendToUser("/notifications/feedback")
    public NotificationResponseDto sendFeedback(@RequestBody final NotificationDto notificationDto,
                                             final Principal principal) {
        notificationDto.setFrom(principal.getName());

        if (notificationDto.getType().equals("4")) {
            Feedback feedback = feedbackService.getFeedbackById(notificationDto.getFeedbackId()).orElse(null);
            notificationDto.setFeedback(feedback);
            notificationDto.setTo("admin");
            notificationDto.setLink("/admin/feedbacks/" + feedback.getId());
        }
        Notification notification = notificationService.addNotification(notificationDto);
        NotificationResponseDto notificationResponseDto = new NotificationResponseDto(
                notification
        );
        return notificationResponseDto;
    }

}
