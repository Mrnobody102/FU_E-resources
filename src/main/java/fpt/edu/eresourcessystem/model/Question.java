package fpt.edu.eresourcessystem.model;


import fpt.edu.eresourcessystem.dto.QuestionDto;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.enums.QuestionAnswerEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("questions")
public class Question {
    @Id
    private String id;

    @NotEmpty(message = "question.validation.question.required")
    private String content;

    @NotNull
    @DocumentReference(lazy = true)
    private Student student;

    @DocumentReference(lazy = true)
    private fpt.edu.eresourcessystem.model.Document documentId;

    @DocumentReference(lazy = true)
    private Set<Answer> answers;

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

    // Constructor DTO
    public Question(QuestionDto questionDTO) {
        this.id = questionDTO.getId();
        this.content = questionDTO.getContent();
        this.student = questionDTO.getStudent();
        this.documentId = questionDTO.getDocumentId();
        this.answers = questionDTO.getAnswers();
        this.lecturer = questionDTO.getLecturer();
        this.deleteFlg = CommonEnum.DeleteFlg.PRESERVED;
        this.status = QuestionAnswerEnum.Status.CREATED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Question question)) return false;
        return Objects.equals(getId(), question.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
