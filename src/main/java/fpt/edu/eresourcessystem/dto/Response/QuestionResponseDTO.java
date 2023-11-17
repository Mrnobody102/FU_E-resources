package fpt.edu.eresourcessystem.dto.Response;

import fpt.edu.eresourcessystem.model.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponseDTO {
    private String questionId;

    private String questionContent;

    private String studentName;

    private String documentId;

    private int totalAnswers;

    private String lecturerName;

    private String lastModifiedDate;
    public  QuestionResponseDTO(Question question){
        this.questionId= question.getId();
        this.questionContent = question.getContent();
        if(null!=question.getStudent()){
            this.studentName= question.getStudent().getAccount().getName();
        }
        this.documentId = question.getDocumentId().getId();

        if(null!=question.getAnswers()){
            this.totalAnswers = question.getAnswers().size();
        }else {
            this.totalAnswers = 0;
        }
        if(null != question.getLecturer() && null !=question.getLecturer().getAccount()) {
            this.lecturerName = question.getLecturer().getAccount().getName();
        }
        this.lastModifiedDate = question.getLastModifiedDate();
    }
}
