package fpt.edu.eresourcessystem.dto;

import fpt.edu.eresourcessystem.enums.DocumentEnum;
import fpt.edu.eresourcessystem.model.ResourceType;
import fpt.edu.eresourcessystem.model.Topic;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DocumentDto {
    private String id;
    @NotNull
    private Topic topic;
    private ResourceType resourceType;

    @NotEmpty(message = "document.validation.title.required")
    private String title;
    private String description;
    private String suffix;
    private ObjectId contentId;
    private String cloudFileLink;
    private String fileName;
    private String content;
    private boolean displayWithFile;
    // Only use when response, no need in requests
    private DocumentEnum.DocumentStatusEnum docStatus;
    private String createdBy;
    private String createdDate;
    private String lastModifiedBy;
    private String lastModifiedDate;

}
