package fpt.edu.eresourcessystem.model;


import fpt.edu.eresourcessystem.dto.StudentNoteDto;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.enums.DocumentEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("student_notes")
public class StudentNote {
    @Id
    private String id;
    @NotNull
    private String studentId;
    private String title;
    private String description;
    private DocumentEnum.DocumentStatusEnum status;

    private DocumentEnum.DocumentFormat docType;

    private String editorContent;

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


    public StudentNote(StudentNoteDto studentNoteDTO) {
        this.id = studentNoteDTO.getId();
        this.studentId = studentNoteDTO.getStudentId();
        this.title = studentNoteDTO.getTitle();
        this.description = studentNoteDTO.getDescription();
        this.editorContent = studentNoteDTO.getEditorContent();
        this.status = studentNoteDTO.getStatus();
        this.deleteFlg = CommonEnum.DeleteFlg.PRESERVED;

    }
}
