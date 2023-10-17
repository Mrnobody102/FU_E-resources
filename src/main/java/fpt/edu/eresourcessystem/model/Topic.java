package fpt.edu.eresourcessystem.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("topics")
@NoArgsConstructor
@AllArgsConstructor
public class Topic {
    @Id
    private String topicId;
    private String courseId;

    private String topicTitle;
    private String topicDescription;
    private List<String> documents;

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
