package fpt.edu.eresourcessystem.model;

import fpt.edu.eresourcessystem.dto.CourseDTO;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.enums.CourseEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Data
@Document("courses")
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    private String id;

    @NotNull
    @DocumentReference
    private Librarian librarian;

    @DocumentReference
    private Lecturer lecturer;

    @Indexed(unique = true)
    @NotEmpty(message = "course.validation.courseCode.required")
    private String courseCode;

    @NotEmpty(message = "course.validation.courseName.required")
    private String courseName;
    private String description;
    @DocumentReference
    private TrainingType trainingType;

    @DocumentReference(lazy = true)
    private List<Topic> topics;
    private List<String> students;

    @NotNull
    private CourseEnum.Status status;
    private List<LecturerCourseId> lecturerCourseIds;

    // Delete flag
    private CommonEnum.DeleteFlg deleteFlg;
    //Audit Log
    @CreatedBy
    private String createdBy;
    @CreatedDate
    private String createdDate;
    @LastModifiedBy
    private String lastModifiedBy;
    @LastModifiedDate
    private String lastModifiedDate;

    // Constructor DTO
    public Course(CourseDTO courseDTO, CourseEnum.Status status){
        this.id = courseDTO.getId();
        this.librarian = courseDTO.getLibrarian();
        this.lecturer = courseDTO.getLecturer();
        this.courseCode = courseDTO.getCourseCode();
        this.courseName = courseDTO.getCourseName();
        this.description = courseDTO.getDescription();
        this.trainingType = courseDTO.getTrainingType();
        this.status = status;
        this.deleteFlg = CommonEnum.DeleteFlg.PRESERVED;
    }

    public Course publishCourse(CourseDTO courseDTO){
        return new Course(courseDTO, CourseEnum.Status.PUBLISH);
    }

    public Course draftCourse(CourseDTO courseDTO){
        return new Course(courseDTO, CourseEnum.Status.DRAFT);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;

        if (lecturer != null ? !lecturer.getId().equals(course.lecturer.getId()) : course.lecturer != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        // Implement a hashCode method if you override equals
        return lecturer != null ? lecturer.getId().hashCode() : 0;
    }
}