package fpt.edu.eresourcessystem.model;


import fpt.edu.eresourcessystem.enums.CourseEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("lecturerCourse")
public class lecturerCourse {
    @Id
    private String lecturerId;

    @Id
    private String courseId;

    private String accountId;
    private String startDate;

    //Audit Log
    @CreatedBy
    private String createdBy;

    @Id
    @CreatedDate
    private String createdDate;
    @LastModifiedBy
    private String lastModifiedBy;

    @Id
    @LastModifiedDate
    private String lastModifiedDate;

}
