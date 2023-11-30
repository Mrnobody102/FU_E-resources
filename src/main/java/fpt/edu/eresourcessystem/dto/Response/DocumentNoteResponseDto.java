package fpt.edu.eresourcessystem.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentNoteResponseDto {
    private String id;
    private String studentId;

    private String content;
    private String docId;

    // Only use when response, no need in requests
    private String createdBy;
    private String createdDate;
    private String lastModifiedBy;
    private String lastModifiedDate;
}
