package fpt.edu.eresourcessystem.model.elasticsearch;

import fpt.edu.eresourcessystem.model.Course;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;

@Document(indexName = "documents")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EsCourse implements Serializable {
    @Id
    private String courseId;
    @Field
    private String code;
    @Field
    private String name;
    @Field
    private String description;
    @Field
    private String lecturer;
    @Field
    private String lastModifiedDate;
    @Field
    private String createdBy;

    public EsCourse(Course course) {
        this.courseId = course.getId();
        this.code = course.getCourseCode();
        this.description = course.getDescription();
        this.name = course.getCourseName();
        this.lecturer = course.getLecturer().getAccount().getName();
        this.lastModifiedDate = course.getLastModifiedDate();
        this.createdBy = course.getCreatedBy();
    }
}