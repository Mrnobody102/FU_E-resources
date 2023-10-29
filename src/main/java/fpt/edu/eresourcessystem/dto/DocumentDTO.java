package fpt.edu.eresourcessystem.dto;

import fpt.edu.eresourcessystem.enums.DocumentEnum;
import fpt.edu.eresourcessystem.model.ResourceType;
import fpt.edu.eresourcessystem.model.Topic;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DocumentDTO {
    private String id;
    @NotNull
    private List<Topic> topics;
    private String resourceType;

    @NotEmpty(message = "document.validation.title.required")
    private String title;
    private String description;
    private String suffix;
    private byte[] content;
    private String contentLink; //link video, audio - cloud

    // Only use when response, no need in requests
    private DocumentEnum.DocumentStatusEnum docStatus;
    private String createdBy;
    private String createdDate;
    private String lastModifiedBy;
    private String lastModifiedDate;

    public List<ResourceType> getResourceTypes(){
        List<ResourceType> resourceTypeDTOs = new ArrayList<>();
        for(Topic topic:topics){
            ResourceTypeDTO resourceTypeDTO = new ResourceTypeDTO(this.resourceType, topic);
            ResourceType resourceType = new ResourceType(resourceTypeDTO);
            resourceTypeDTOs.add(resourceType);
        }
        return resourceTypeDTOs;
    }
}
