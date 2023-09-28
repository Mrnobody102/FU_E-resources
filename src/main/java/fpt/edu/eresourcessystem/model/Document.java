package fpt.edu.eresourcessystem.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.catalina.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

@org.springframework.data.mongodb.core.mapping.Document("courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Document {
    @Id
    private String documentId;

    private String title;
    private String content;  // or perhaps a URL to the content if stored externally

    @DBRef
    private Course course;  // Reference to the course the document is associated with

    @DBRef
    private Account uploader;  // Reference to the user who uploaded the document

}