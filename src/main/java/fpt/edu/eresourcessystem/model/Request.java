package fpt.edu.eresourcessystem.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("requests")
public class Request {
    @Id
    private String requestId;
    private String studentId;

    private String request;
    private String requestDescription;
    private String topicId;
    private String courseId;
    private String majorId;
    private List<String> lecturers;

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