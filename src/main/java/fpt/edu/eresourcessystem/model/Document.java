package fpt.edu.eresourcessystem.model;


import fpt.edu.eresourcessystem.dto.DocumentDTO;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.enums.DocumentEnum;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@org.springframework.data.mongodb.core.mapping.Document("documents")
public class Document {
    @Id
    private String id;

    @NotEmpty(message = "course.validation.resourceType.required")
    @DocumentReference(lazy = true)
    private List<ResourceType> resourceTypes;

    @NotEmpty(message = "course.validation.title.required")
    private String title;
    private String description;
    private DocumentEnum.DocumentStatusEnum docStatus;

    private DocumentEnum.DocumentFormat docType;
    private String suffix;

    private byte[] content;

    private String contentLink; //link video, audio - cloud

    private List<String> notes;
    private List<String> questions;

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

    public Document(DocumentDTO documentDTO) {
        this.id = documentDTO.getId();
        this.resourceTypes = documentDTO.getResourceTypes();
        this.title = documentDTO.getTitle();
        this.description = documentDTO.getDescription();
        this.docStatus = documentDTO.getDocStatus();
        this.suffix = documentDTO.getSuffix();
        this.docType = DocumentEnum.DocumentFormat.getDocType(documentDTO.getSuffix());
        this.deleteFlg = CommonEnum.DeleteFlg.PRESERVED;
    }
}