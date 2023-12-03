package fpt.edu.eresourcessystem.dto.Response;

import fpt.edu.eresourcessystem.model.Document;
import fpt.edu.eresourcessystem.model.Topic;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponseDto {
    private String id;
    private String title;
    private ObjectId topic;
    private String description;
    private String createdBy;
    private String createdDate;
    private String lastModifiedBy;
    private String lastModifiedDate;
    public DocumentResponseDto(Document document) {
        this.id = document.getId();
        if (null != document.getTopic()) {
//            this.topicId = document.getTopic().getId();
//            this.topicTitle = document.getTopic().getTopicTitle();
            this.topic = getTopic();
        }
        this.title = document.getTitle();
        this.description = document.getDescription();
        this.lastModifiedDate = document.getLastModifiedDate();
    }
}
