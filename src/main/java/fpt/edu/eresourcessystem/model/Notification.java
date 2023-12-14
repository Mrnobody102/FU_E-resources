package fpt.edu.eresourcessystem.model;

import fpt.edu.eresourcessystem.dto.NotificationDto;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.enums.NotificationEnum;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("notifications")
public class Notification {
    @Id
    private String id;
    @NotNull
    private String fromAccount;

    private String toAccount;

    private String notificationContent;


    // Notification related objects
    @DocumentReference(lazy = true)
    private Course course;
    @DocumentReference(lazy = true)
    private fpt.edu.eresourcessystem.model.Document document;
    @DocumentReference(lazy = true)
    private Feedback feedback;
    @DocumentReference(lazy = true)
    private Question question;

    private String linkToView;
    private NotificationEnum.NotificationStatus notificationStatus;
    private NotificationEnum.NotificationType notificationType;

    // Delete flag
    private CommonEnum.DeleteFlg deleteFlg;
    //Audit Log
    @CreatedBy
    private String createdBy;
    @CreatedDate
    private String createdDate;
    @LastModifiedBy
    private String lastModifiedBy;
    @LastModifiedDate
    private String lastModifiedDate;

    public Notification(NotificationDto notificationDto) {
        this.id = notificationDto.getId();
        this.fromAccount = notificationDto.getFrom();
        this.toAccount = notificationDto.getTo();
        this.notificationType = NotificationEnum.NotificationType.getType(notificationDto.getType());
        if (NotificationEnum.NotificationType.getType(notificationDto.getType()) == NotificationEnum.NotificationType.STUDENT_ASK_ON_DOCUMENT
                || NotificationEnum.NotificationType.getType(notificationDto.getType()) == NotificationEnum.NotificationType.STUDENT_REPLY_ON_DOCUMENT
                || NotificationEnum.NotificationType.getType(notificationDto.getType()) == NotificationEnum.NotificationType.LECTURER_REPLY_ON_DOCUMENT) {
            this.document = notificationDto.getDocument();
            // question
            this.notificationContent = notificationDto.getFrom()
                    + NotificationEnum.NotificationType.getStringType(notificationDto.getType())
                    + notificationDto.getDocument().getTitle();
        }
        if (NotificationEnum.NotificationType.getType(notificationDto.getType())
                == NotificationEnum.NotificationType.USER_SEND_FEEDBACK) {
            this.feedback = notificationDto.getFeedback();
            this.notificationContent = notificationDto.getFrom()
                    + NotificationEnum.NotificationType.getStringType(notificationDto.getType());
        }
        if (NotificationEnum.NotificationType.getType(notificationDto.getType())
                == NotificationEnum.NotificationType.LECTURER_UPDATE_COURSE) {
            this.course = notificationDto.getCourse();
            this.notificationContent = "Lecturer "
                    + NotificationEnum.NotificationType.getStringType(notificationDto.getType()) + notificationDto.getCourse().getCourseCode();
        }
        this.linkToView = notificationDto.getLink();
        this.notificationStatus = NotificationEnum.NotificationStatus.UNREAD;
        this.deleteFlg = CommonEnum.DeleteFlg.PRESERVED;
    }
}
