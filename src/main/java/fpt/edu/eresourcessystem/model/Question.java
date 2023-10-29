package fpt.edu.eresourcessystem.model;


import fpt.edu.eresourcessystem.enums.CommonEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("questions")
public class Question {
    @Id
    private String id;
    @NotEmpty(message = "question.validation.question.required")
    private String question;
    @NotNull
    @DocumentReference(lazy = true)
    private Student student;
    @NotNull
    @DocumentReference(lazy = true)
    private fpt.edu.eresourcessystem.model.Document documentId;

    // Answer
    private String answer;
    @DocumentReference(lazy = true)
    private Lecturer lecturer;

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

    // Constructor DTO
}
