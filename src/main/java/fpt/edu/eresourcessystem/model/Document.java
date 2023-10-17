package fpt.edu.eresourcessystem.model;


import fpt.edu.eresourcessystem.enums.DocumentEnum;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@org.springframework.data.mongodb.core.mapping.Document("document")
public class Document {
    @Id
    private String documentId;

    private String title;
    private String description;
    private String status;

    private String courseId;
    private String topicId;  // Reference to the folder that contain it
    private String uploaderId;  // Reference to the user who uploaded the document

    private String content;  // doc, audio, video
    // để nhiều trường có thể lưu nhiều dạng tài liệu, file...

    private String note;
    private DocumentEnum.DocumentAccessLevelEnum accessLevel;

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