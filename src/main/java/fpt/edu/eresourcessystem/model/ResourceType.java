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

import java.time.LocalDate;
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
    public ResourceType(String getResourceTypeName) {
        this.resourceTypeName = getResourceTypeName;
        this.deleteFlg = CommonEnum.DeleteFlg.PRESERVED;
    }
}
