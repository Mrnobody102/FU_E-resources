package fpt.edu.eresourcessystem.model;

import fpt.edu.eresourcessystem.dto.AnswerDto;
import fpt.edu.eresourcessystem.dto.QuestionDto;
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
    private Question questionId;

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
    public Answer(AnswerDto answerDto) {
        this.id = answerDto.getId();
        this.answer = answerDto.getAnswer();
        this.student = answerDto.getStudent();
        this.documentId = answerDto.getDocumentId();
        this.questionId = answerDto.getQuestionId();
        this.lecturer = answerDto.getLecturer();
        this.deleteFlg = CommonEnum.DeleteFlg.PRESERVED;
    }
}
