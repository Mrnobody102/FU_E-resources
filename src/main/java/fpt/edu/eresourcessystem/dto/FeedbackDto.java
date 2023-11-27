package fpt.edu.eresourcessystem.dto;


import fpt.edu.eresourcessystem.model.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("feedbacks")
public class FeedbackDto {
    @Id
    private String id;
    private Account account;
    private String feedbackEmotion;
    private String feedbackContent;

    // Only use when response, no need in requests
    private String createdBy;
    private String createdDate;
    private String lastModifiedBy;
    private String lastModifiedDate;
}