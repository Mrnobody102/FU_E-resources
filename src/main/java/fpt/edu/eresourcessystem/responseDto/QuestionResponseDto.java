package fpt.edu.eresourcessystem.responseDto;

import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.model.Answer;
import fpt.edu.eresourcessystem.model.Lecturer;
import fpt.edu.eresourcessystem.model.Question;
import fpt.edu.eresourcessystem.model.Student;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponseDto {
    private String id;

    private String questionContent;

    private String studentName;

    private String documentId;

    private int totalAnswers;

    private String lecturerName;

    private String lastModifiedDate;
    public  QuestionResponseDto(Question question){
        this.id= question.getId();
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
