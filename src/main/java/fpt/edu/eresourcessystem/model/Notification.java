package fpt.edu.eresourcessystem.model;

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
    @DocumentReference(lazy = true)
    private Account fromAccount;

    @NotNull
    @DocumentReference(lazy = true)
    private Account toAccount;

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
    private String createdDate;
    @LastModifiedBy
    private String lastModifiedBy;
    @LastModifiedDate
    private String lastModifiedDate;


}
