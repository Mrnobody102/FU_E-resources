package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.dto.NotificationDto;
import fpt.edu.eresourcessystem.dto.Response.NotificationResponseDto;
import fpt.edu.eresourcessystem.enums.NotificationEnum;
import fpt.edu.eresourcessystem.model.Notification;

import java.util.List;


public interface NotificationService {
    Notification findById(String noId);

    Notification updateNotification(Notification notification);

    List<NotificationResponseDto> findByToAccount(String email);

    List<NotificationResponseDto> findAllByToAccount(String email);

    List<NotificationResponseDto> findUnreadByToAccount(String email);

    Notification addNotification(NotificationDto notificationDto);

    Notification addNotificationWhenUpdateDocument(Notification notification);

    void markReadAll(String email);

    void deleteNotification(List<String> ids);

    void updateNotificationStatus(NotificationEnum.NotificationStatus oldStatus, NotificationEnum.NotificationStatus newStatus);
}
