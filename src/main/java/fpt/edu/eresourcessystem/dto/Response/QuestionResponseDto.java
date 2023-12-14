package fpt.edu.eresourcessystem.dto.Response;

import fpt.edu.eresourcessystem.model.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponseDto {
    private String questionId;

    private String questionContent;

    private String studentName;

    private String documentId;

    private String documentTitle;

    private int totalAnswers;

    private String lecturerEmail;
    private String createdDate;
    private String lastModifiedDate;
    public QuestionResponseDto(Question question){
        this.questionId= question.getId();
        this.questionContent = question.getContent();
        if(null!=question.getStudent()){
            this.studentName= question.getStudent().getAccount().getName();
        }
        this.documentId = question.getDocumentId().getId();
        this.documentTitle = question.getDocumentId().getTitle();
        if(null!=question.getAnswers()){
            this.totalAnswers = question.getAnswers().size();
        }else {
            this.totalAnswers = 0;
        }
        this.lecturerEmail = question.getLecturer();
        this.createdDate = question.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.lastModifiedDate = question.getLastModifiedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionResponseDto that)) return false;
        return Objects.equals(getQuestionId(), that.getQuestionId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getQuestionId());
    }
}
