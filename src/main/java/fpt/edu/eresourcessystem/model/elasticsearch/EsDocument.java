package fpt.edu.eresourcessystem.model.elasticsearch;

import fpt.edu.eresourcessystem.dto.DocumentDTO;
import fpt.edu.eresourcessystem.enums.DocumentEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;

@Document(indexName = "documents")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class EsDocument implements Serializable {
    @Id
    private String documentId;
    @Field
    private String title;
    @Field
    private String description;
    @Field
    private String docType;
    @Field
    private String editorContent;
    @Field
    private String lastModifiedDate;

    public EsDocument(fpt.edu.eresourcessystem.model.Document document) {
        this.documentId = document.getId();
        this.title = document.getTitle();
        this.description = document.getDescription();
        this.docType = document.getSuffix().toUpperCase();
        this.editorContent = document.getEditorContent();
        this.lastModifiedDate = document.getLastModifiedDate();
    }
}