package fpt.edu.eresourcessystem.dto.Response;

import fpt.edu.eresourcessystem.model.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {
    private String id;
    private String notificationContent;
    private String questionContent;
    private String lastModifiedDate;
    private String link;

    public NotificationResponseDto(Notification notification) {
        this.id = notification.getId();
        this.notificationContent = notification.getNotificationContent();
        this.questionContent = notification.getContent();
        this.link = notification.getLinkToView();
        this.lastModifiedDate = notification.getLastModifiedDate();
    }
}
