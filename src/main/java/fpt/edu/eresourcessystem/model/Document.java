package fpt.edu.eresourcessystem.model;


import fpt.edu.eresourcessystem.enums.DocumentEnum;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@org.springframework.data.mongodb.core.mapping.Document("documents")
public class Document {
    @Id
    private String documentId;
    private String topicId;
    private String courseId;

    private String title;
    private String description;
    private DocumentEnum.DocumentStateEnum docStatus;
    private DocumentEnum.DocumentAccessLevelEnum accessLevel;

    private DocumentEnum.DocumentFormat docType;
    private byte[] content;

    private List<String> notes;
    private List<String> questions;

    //Audit Log
    @CreatedBy
    private String createdBy;
    @CreatedDate
    private String createdDate;
    @LastModifiedBy
    private String lastModifiedBy;
    @LastModifiedDate
    private String lastModifiedDate;
}