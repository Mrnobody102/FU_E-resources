package fpt.edu.eresourcessystem.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteDTO {
    @Id
    private String id;
    private String studentId;
    private String documentId;

    private String note;

    // Only use when response, no need in requests
    private String createdBy;
    private String createdDate;
    private String lastModifiedBy;
    private String lastModifiedDate;
}
