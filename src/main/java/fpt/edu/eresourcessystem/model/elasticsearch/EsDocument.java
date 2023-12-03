package fpt.edu.eresourcessystem.model.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;

@Document(indexName = "documents")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EsDocument implements Serializable {
    @Id
    private String documentId;
    @Field
    private String title;
    @Field
    private String description;
    @Field
    private String content;
    @Field
    private String docType;
    @Field
    private String lastModifiedDate;
    @Field
    private String createdBy;

    public EsDocument(fpt.edu.eresourcessystem.model.Document document) {
        this.documentId = document.getId();
        this.title = document.getTitle();
        this.description = document.getDescription();
        this.content = document.getContent();
        this.docType = document.getSuffix().toUpperCase();
        this.lastModifiedDate = document.getLastModifiedDate();
        this.createdBy = document.getCreatedBy();
    }
}