package fpt.edu.eresourcessystem.model;

import fpt.edu.eresourcessystem.dto.AnswerDTO;
import fpt.edu.eresourcessystem.dto.QuestionDTO;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("answers")
public class Answer {
    @Id
    private String id;

    @NotEmpty(message = "question.validation.question.required")
    private String answer;

    @NotNull
    @DocumentReference(lazy = true)
    private Student student;

    @NotNull
    @DocumentReference(lazy = true)
    private fpt.edu.eresourcessystem.model.Document documentId;

    @NotNull
    @DocumentReference(lazy = true)
    private Question question;

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
    public Answer(AnswerDTO answerDTO) {
        this.id = answerDTO.getId();
        this.answer = answerDTO.getAnswer();
        this.student = answerDTO.getStudent();
        this.documentId = answerDTO.getDocumentId();
        this.question = answerDTO.getQuestionId();
        this.lecturer = answerDTO.getLecturer();
        this.deleteFlg = CommonEnum.DeleteFlg.PRESERVED;
    }
}
