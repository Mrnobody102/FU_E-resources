package fpt.edu.eresourcessystem.dto;

import fpt.edu.eresourcessystem.enums.CourseEnum;
import fpt.edu.eresourcessystem.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    @Id
    private String id;
    private Librarian librarian;
    private Lecturer lecturer;

    @Indexed(unique = true)
    private String courseCode;

    private String courseName;
    private String description;
    private TrainingType trainingType;
    private CourseEnum.Status status;

    // Only use when response, no need in requests
    private List<Topic> topics;
    private List<Student> students;
    private List<LecturerCourseId> lecturerCourseIds;
    private String createdBy;
    private String createdDate;
    private String lastModifiedBy;
    private String lastModifiedDate;
}