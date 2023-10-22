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
    private String managerId;

    @Indexed(unique = true)
    private String courseCode;

    private String courseName;
    private String description;

    private List<String> topics;
    private List<String> students;

    private CourseEnum.Status status;
    private CourseEnum.DeleteFlag deleteFlag;
    private List<LecturerCourseId> lecturerCourseIds;

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