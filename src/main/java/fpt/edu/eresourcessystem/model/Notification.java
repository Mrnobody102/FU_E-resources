package fpt.edu.eresourcessystem.model;

import fpt.edu.eresourcessystem.enums.NotificationEnum;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;

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

    @NotNull
    @DocumentReference(lazy = true)
    private String toAccount;

    @NotNull
    private String content;

    @DocumentReference(lazy = true)
    private Course course;

    @DocumentReference(lazy = true)
    private fpt.edu.eresourcessystem.model.Document document;

    private String linkToView;

    @DocumentReference(lazy = true)
    private Question question;
    private NotificationEnum.NotificationStatus notificationStatus;
    private NotificationEnum.NotificationType notificationType;
    //Audit Log
    @CreatedBy
    private String createdBy;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedBy
    private String lastModifiedBy;
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    public Notification(String fromAccount, String toAccount, String content, String linkToView) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.content = content;
        this.linkToView = linkToView;
    }
}
