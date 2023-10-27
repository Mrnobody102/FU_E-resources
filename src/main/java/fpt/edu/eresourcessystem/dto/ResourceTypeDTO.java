package fpt.edu.eresourcessystem.dto;


import fpt.edu.eresourcessystem.model.Topic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceTypeDTO {
    @Id
    private String id;
    private String resourceTypeName;
    private Topic topic;

    // Only use when response, no need in requests
    private List<String> documents;
    private String createdBy;
    private String createdDate;
    private String lastModifiedBy;
    private String lastModifiedDate;

    public ResourceTypeDTO(String resourceTypeName, Topic topic) {
        this.resourceTypeName = resourceTypeName;
        this.topic = topic;
    }
}
