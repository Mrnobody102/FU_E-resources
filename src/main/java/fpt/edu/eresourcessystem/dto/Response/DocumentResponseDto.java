package fpt.edu.eresourcessystem.dto.Response;

import fpt.edu.eresourcessystem.model.Document;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponseDto {
    private String id;
    private String title;
    private String topicId;
    private String topicTitle;
    private String description;
    private String createdBy;
    private String createdDate;
    private String lastModifiedBy;
    private String lastModifiedDate;
    public DocumentResponseDto(Document document) {
        this.id = document.getId();
        this.title = document.getTitle();
        if(null!= document.getTopic()){
            topicId = document.getTopic().getId();
            topicTitle = document.getTopic().getTopicTitle();
        }
        this.description = document.getDescription();
        this.lastModifiedDate = document.getLastModifiedDate();
        this.lastModifiedBy = document.getLastModifiedBy();
    }
}
