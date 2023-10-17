package fpt.edu.eresourcessystem.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("feedbacks")
public class Feedback {
    @Id
    private String feedbackId;
    private String studentId;
    private String feedbackEmotion;
    private String feedbackContent;

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