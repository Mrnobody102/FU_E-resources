package fpt.edu.eresourcessystem.model;


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
@Document("notes")
public class Note {
    @Id
    private String id;
    @NotNull
    private String studentId;
    @NotNull
    private String documentId;

    @NotEmpty(message = "note.validation.note.required")
    private String note;

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

    public Note(String id, @NotNull String studentId, @NotNull String documentId, String note) {
        this.id = id;
        this.studentId = studentId;
        this.documentId = documentId;
        this.note = note;
    }
}
