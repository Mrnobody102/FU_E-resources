package fpt.edu.eresourcessystem.dto;


import fpt.edu.eresourcessystem.enums.DocumentEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentNoteDto {
    @Id
    private String id;
    private String studentId;
    private String title;
    private String description;
    private DocumentEnum.DocumentStatusEnum status;

    private DocumentEnum.DocumentFormat docType;

    private String editorContent;

    // Only use when response, no need in requests
    private String createdBy;
    private String createdDate;
    private String lastModifiedBy;
    private String lastModifiedDate;
}
