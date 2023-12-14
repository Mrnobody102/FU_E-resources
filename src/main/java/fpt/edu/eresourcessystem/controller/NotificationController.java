package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.dto.NotificationDto;
import fpt.edu.eresourcessystem.dto.QuestionDto;
import fpt.edu.eresourcessystem.dto.Response.NotificationResponseDto;
import fpt.edu.eresourcessystem.dto.Response.QuestionResponseDto;
import fpt.edu.eresourcessystem.enums.NotificationEnum;
import fpt.edu.eresourcessystem.model.*;
import fpt.edu.eresourcessystem.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.List;

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
            notificationDto.setLink("/lecturer/documents/" + question.getDocumentId().getId() + "#" + question.getId());
        }
        if (notificationDto.getType().equals("3")) {
            Answer answer = answerService.findById(notificationDto.getAnswerId());
            notificationDto.setDocument(answer.getDocumentId());
            notificationDto.setTo(answer.getDocumentId().getCreatedBy());
            notificationDto.setLink("/lecturer/documents/" + answer.getDocumentId().getId() + "#" + answer.getQuestion().getId());
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
            notificationDto.setLink("/student/documents/" + answer.getDocumentId().getId() + "#" + answer.getQuestion().getId());
        }
        Notification notification = notificationService.addNotification(notificationDto);
        NotificationResponseDto notificationResponseDto = new NotificationResponseDto(
                notification
        );
        return notificationResponseDto;
    }

    @MessageMapping("/question")
    @SendToUser("/notifications/question")
    public QuestionResponseDto sendRealtimeQuestion(final String qId) {
        Question question = questionService.findById(qId);
        return new QuestionResponseDto(question);
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

    @GetMapping("/notifications/{nId}")
    public String viewNotification(@PathVariable String nId) {
        Notification notification = notificationService.findById(nId);
        notification.setNotificationStatus(NotificationEnum.NotificationStatus.READ);
        notificationService.updateNotification(notification);
        return "redirect:" + notification.getLinkToView();
    }

    @GetMapping("/notifications/mark_read_all")
    public String markReadAll() {
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        notificationService.markReadAll(loggedInEmail);
        return "redirect:/lecturer/notifications";
    }

    @PostMapping("/notifications/delete")
    public String deleteNotifications(@RequestBody List<String> notificationIds) {
        notificationService.deleteNotification(notificationIds);
        return "redirect:/lecturer/notifications";
    }


}
