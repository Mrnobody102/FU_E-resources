package fpt.edu.eresourcessystem.dto.Response;

import fpt.edu.eresourcessystem.model.Document;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponseDto {
    private String docId;
    private String topicId;
    private String docTitle;
    private String description;
    private String createdBy;
    private String createdDate;
    private String lastModifiedBy;
    private String lastModifiedDate;

    public DocumentResponseDto(Document document){
        this.docId = document.getId();
        if(null!=document.getTopic()){
            this.topicId = document.getTopic().getId();
        }
        this.docTitle =document.getTitle();
        this.description = document.getDescription();
        this.lastModifiedDate = document.getLastModifiedDate();
    }
}
