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
    private String description;
    private String status;

    private String courseId;  // Reference to the course the document is associated with

    private String uploaderId;  // Reference to the user who uploaded the document

    private String content;  // doc, audio, video
    // để nhiều trường có thể lưu nhiều dạng tài liệu, file...
}