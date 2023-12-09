package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.dto.NotificationDto;
import fpt.edu.eresourcessystem.dto.Response.NotificationResponseDto;
import fpt.edu.eresourcessystem.enums.NotificationEnum;
import fpt.edu.eresourcessystem.model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


public interface NotificationService {
    Notification findById(String noId);

    Notification updateNotification(Notification notification);

    List<NotificationResponseDto> findByToAccount(String email);

    List<NotificationResponseDto> findAllByToAccount(String email);

    Notification addNotification(NotificationDto notificationDto);
    void updateNotificationStatus(NotificationEnum.NotificationStatus oldStatus, NotificationEnum.NotificationStatus newStatus);
}
