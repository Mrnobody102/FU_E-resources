package fpt.edu.eresourcessystem.model;

import fpt.edu.eresourcessystem.dto.FeedbackDto;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("feedbacks")
public class Feedback {
    @Id
    private String id;
    @NotNull
    @DocumentReference(lazy = true)
    private Account account;

    private String feedbackEmotion;
    @NotEmpty(message = "feedback.validation.content.required")
    private String feedbackContent;

    // Delete flag
    private CommonEnum.DeleteFlg deleteFlg;

    private String status;
    //Audit Log
    @CreatedBy
    private String createdBy;
    @CreatedDate
    private String createdDate;
    @LastModifiedBy
    private String lastModifiedBy;

    public Feedback(FeedbackDto feedbackDTO) {
        this.id = feedbackDTO.getId();
        this.account = feedbackDTO.getAccount();
        this.feedbackEmotion = feedbackDTO.getFeedbackEmotion();
        this.feedbackContent = feedbackDTO.getFeedbackContent();
        this.deleteFlg = CommonEnum.DeleteFlg.PRESERVED;;
        this.status = feedbackDTO.getStatus();
    }

    @LastModifiedDate
    private String lastModifiedDate;

    // Constructor DTO
//    public Feedback(FeedbackDTO feedbackDTO) {
//        this.id = feedbackDTO.getId();
//        this.feedbackEmotion = feedbackDTO.getFeedbackEmotion();
//        this.trainingTypeDescription = trainingTypeDTO.getTrainingTypeDescription();
//        this.deleteFlg = CommonEnum.DeleteFlg.PRESERVED;
//    }
}