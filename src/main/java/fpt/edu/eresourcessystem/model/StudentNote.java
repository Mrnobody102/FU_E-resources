package fpt.edu.eresourcessystem.model;


import fpt.edu.eresourcessystem.dto.DocumentNoteDTO;
import fpt.edu.eresourcessystem.dto.StudentNoteDTO;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.enums.DocumentEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
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
    private String suffix;

    private byte[] content;

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


    public StudentNote(StudentNoteDTO studentNoteDTO) {
        this.id = studentNoteDTO.getId();
        this.studentId = studentNoteDTO.getStudentId();
        this.title = studentNoteDTO.getTitle();
        this.description = studentNoteDTO.getDescription();
        this.content = studentNoteDTO.getContent();
        this.editorContent = studentNoteDTO.getEditorContent();
        this.status = studentNoteDTO.getStatus();
        this.suffix = studentNoteDTO.getSuffix();
        this.docType = DocumentEnum.DocumentFormat.getDocType(studentNoteDTO.getSuffix());
        this.deleteFlg = CommonEnum.DeleteFlg.PRESERVED;

    }
}
