package fpt.edu.eresourcessystem.model;


import fpt.edu.eresourcessystem.dto.DocumentNoteDTO;
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
@Document("document_notes")
public class DocumentNote {
    @Id
    private String id;
    @NotNull
    private String studentId;

    @NotEmpty(message = "studentNote.validation.content.required")
    private String noteContent;

    @NotNull
    private String docId;

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
    public DocumentNote(DocumentNoteDTO documentNoteDTO) {
        this.id = documentNoteDTO.getId();
        this.studentId = documentNoteDTO.getStudentId();
        this.noteContent = documentNoteDTO.getContent();
        this.docId = documentNoteDTO.getDocId();
        this.deleteFlg = CommonEnum.DeleteFlg.PRESERVED;
    }
}
