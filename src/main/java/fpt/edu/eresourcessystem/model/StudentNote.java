package fpt.edu.eresourcessystem.model;


import fpt.edu.eresourcessystem.dto.DocumentNoteDTO;
import fpt.edu.eresourcessystem.dto.StudentNoteDTO;
import fpt.edu.eresourcessystem.enums.CommonEnum;
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
    @NotEmpty(message = "note.validation.note.required")
    private String content;

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

    public StudentNote(String id, @NotNull String studentId, String content) {
        this.id = id;
        this.studentId = studentId;
        this.content = content;
    }

    public StudentNote(StudentNoteDTO studentNoteDTO) {
        this.id = studentNoteDTO.getId();
        this.studentId = studentNoteDTO.getStudentId();
        this.content = studentNoteDTO.getContent();
        this.deleteFlg = CommonEnum.DeleteFlg.PRESERVED;
    }
}
