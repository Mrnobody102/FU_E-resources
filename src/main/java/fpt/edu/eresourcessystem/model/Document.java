package fpt.edu.eresourcessystem.model;

import fpt.edu.eresourcessystem.dto.DocumentDTO;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.enums.DocumentEnum;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@org.springframework.data.mongodb.core.mapping.Document("documents")
public class Document {
    @Id
    private String id;

    @NotEmpty(message = "course.validation.resourceType.required")
    @DocumentReference(lazy = true)
    private Topic topic;
    @DocumentReference(lazy = true)
    private ResourceType resourceType;

    @NotEmpty(message = "course.validation.title.required")
    private String title;
    private String description;
    private DocumentEnum.DocumentStatusEnum docStatus;

    private DocumentEnum.DocumentFormat docType;
    private String suffix;

    // thay bằng grid fs id
    private byte[] content;

    private String editorContent; //link video, audio - cloud

    private List<String> notes;
    private List<String> questions;

    @DocumentReference(lazy = true)
    private List<Rate> rates;

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

    public Document(DocumentDTO documentDTO) {
        this.id = documentDTO.getId();
        this.topic = documentDTO.getTopic();
        this.resourceType = documentDTO.getResourceType();
        this.title = documentDTO.getTitle();
        this.description = documentDTO.getDescription();
        this.content = documentDTO.getContent();
        this.editorContent = documentDTO.getEditorContent();
        this.docStatus = documentDTO.getDocStatus();
        this.suffix = documentDTO.getSuffix();
        this.docType = DocumentEnum.DocumentFormat.getDocType(documentDTO.getSuffix());
        this.deleteFlg = CommonEnum.DeleteFlg.PRESERVED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Document document)) return false;
        return Objects.equals(getId(), document.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public int getRate(){
        int sum = 0;
        for(Rate rate:this.rates){
            sum =+ rate.getRate();
        }
        return sum/this.rates.size();
    }
}