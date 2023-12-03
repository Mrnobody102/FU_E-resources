package fpt.edu.eresourcessystem.model;


import fpt.edu.eresourcessystem.dto.DocumentNoteDto;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
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

    @NotNull
    private String documentTitle;

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
    public DocumentNote(DocumentNoteDto documentNoteDTO) {
        this.id = documentNoteDTO.getId();
        this.studentId = documentNoteDTO.getStudentId();
        this.noteContent = documentNoteDTO.getContent();
        this.docId = documentNoteDTO.getDocId();
        this.documentTitle = documentNoteDTO.getDocumentTitle();
        this.deleteFlg = CommonEnum.DeleteFlg.PRESERVED;
    }
}
