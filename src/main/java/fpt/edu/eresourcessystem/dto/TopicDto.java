package fpt.edu.eresourcessystem.dto;


import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.Document;
import fpt.edu.eresourcessystem.model.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicDto {
    @Id
    private String id;
    private Course course;

    private String topicTitle;
    private String topicDescription;

    // Only use when response, no need in requests
    private List<ResourceType> resourceTypes; // NEW
    private List<Document> documents;
    private String createdBy;
    private String createdDate;
    private String lastModifiedBy;
    private String lastModifiedDate;
}
