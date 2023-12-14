package fpt.edu.eresourcessystem.dto.Response;

import fpt.edu.eresourcessystem.model.Answer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResponseDto {

    private String answerId;

    private String questionId;

    private String answerContent;

    private String studentName;

    private String documentId;

    private int totalAnswers;

    private String lecturerName;
    private String createdDate;
    private String lastModifiedDate;
    public AnswerResponseDto(Answer answer){
        this.answerId= answer.getId();
        this.answerContent = answer.getAnswer();
        if(null!=answer.getStudent() && null!=answer.getStudent().getAccount()){
            this.studentName= answer.getStudent().getAccount().getName();
        }
        this.documentId = answer.getDocumentId().getId();

        this.questionId = answer.getQuestion().getId();
        if(null != answer.getLecturer() && null !=answer.getLecturer().getAccount()) {
            this.lecturerName = answer.getLecturer().getAccount().getName();
        }
        this.createdDate = answer.getCreatedDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        this.lastModifiedDate = answer.getLastModifiedDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }
}
