package fpt.edu.eresourcessystem.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestDTO {
    @Id
    private String id;
    private String studentId;

    private String request;
    private String requestDescription;
    private String topicId;
    private String courseId;

    // Only use when response, no need in requests
    private List<String> lecturers;
    private String createdBy;
    private String createdDate;
    private String lastModifiedBy;
    private String lastModifiedDate;
}