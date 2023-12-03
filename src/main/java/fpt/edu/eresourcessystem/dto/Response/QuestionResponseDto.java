package fpt.edu.eresourcessystem.dto.Response;

import fpt.edu.eresourcessystem.model.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
        this.lastModifiedDate = question.getLastModifiedDate();
    }
}
