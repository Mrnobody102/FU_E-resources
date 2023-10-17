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
@Document("courses")
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    private String courseId;

    @Indexed(unique = true)
    private String courseCode;

    private String courseName;
    private String description;

    private List<String> topics;
    private List<String> lecturers;
    private List<String> students;

    private CourseEnum.Major major;
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