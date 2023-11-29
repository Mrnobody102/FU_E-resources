package fpt.edu.eresourcessystem.model;

import fpt.edu.eresourcessystem.dto.AnswerDto;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.enums.QuestionAnswerEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Objects;

@Getter
@Setter
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

    private QuestionAnswerEnum.Status status;

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
    public Answer(AnswerDto answerDTO) {
        this.id = answerDTO.getId();
        this.answer = answerDTO.getAnswer();
        this.student = answerDTO.getStudent();
        this.documentId = answerDTO.getDocumentId();
        this.question = answerDTO.getQuestionId();
        this.lecturer = answerDTO.getLecturer();
        this.deleteFlg = CommonEnum.DeleteFlg.PRESERVED;
        this.status = QuestionAnswerEnum.Status.CREATED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Answer answer)) return false;
        return Objects.equals(getId(), answer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
