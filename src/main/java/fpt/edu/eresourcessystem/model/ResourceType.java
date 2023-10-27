package fpt.edu.eresourcessystem.model;


import fpt.edu.eresourcessystem.dto.ResourceTypeDTO;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Data
@Document("resource_types")
@NoArgsConstructor
@AllArgsConstructor
public class ResourceType {
    @Id
    private String id;
    @NotEmpty
    private String resourceTypeName;
    @NotNull
    @DocumentReference(lazy = true)
    private Topic topic;
    @DocumentReference(lazy = true)
    private List<String> documents;

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
    public ResourceType(ResourceTypeDTO resourceTypeDTO) {
        this.resourceTypeName = resourceTypeDTO.getResourceTypeName();
        this.topic = resourceTypeDTO.getTopic();
    }
}
