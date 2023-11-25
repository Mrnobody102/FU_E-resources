package fpt.edu.eresourcessystem.model;


import fpt.edu.eresourcessystem.dto.ResourceTypeDTO;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Document("resource_types")
@NoArgsConstructor
@AllArgsConstructor
public class ResourceType {
    @Id
    private String id;
    @NotEmpty
    private String resourceTypeName;
    @DocumentReference(lazy = true)
    private Course course;

    @DocumentReference(lazy = true)
    private List<fpt.edu.eresourcessystem.model.Document> documents;

    // Delete flag
    private CommonEnum.DeleteFlg deleteFlg;
    private LocalDate deletedDate;
    private Account deletedBy;

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
    public ResourceType(ResourceType resourceType) {
        this.resourceTypeName = resourceType.getResourceTypeName();
        this.course = resourceType.getCourse();
        this.deleteFlg = CommonEnum.DeleteFlg.PRESERVED;
    }

    public ResourceType(String name, Course course) {
        this.resourceTypeName = name;
        this.course = course;
        this.deleteFlg = CommonEnum.DeleteFlg.PRESERVED;
    }
}
