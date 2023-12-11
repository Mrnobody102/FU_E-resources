package fpt.edu.eresourcessystem.model;

import fpt.edu.eresourcessystem.dto.CourseLogDto;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.enums.CourseEnum;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("course_log")
public class CourseLog {
    @Id
    private String id;
    private String courseId;
    private String courseCode;
    private String courseName;
    private String oldContent;
    private String newContent;
    private CourseEnum.LecturerAction action;

    // Delete flag
    private CourseEnum.CourseObject object;
    private String objectId;
    private String objectName;
    private CommonEnum.DeleteFlg deleteFlg;
    private String email;
    @CreatedBy
    private String createdBy;
    @CreatedDate
    private LocalDateTime createdDate;
    public CourseLog(String courseId, String courseCode, String courseName, CourseEnum.LecturerAction action,
                     CourseEnum.CourseObject object, String objectId, String objectName, String email,
                     String oldContent, String newContent) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.action = action;
        this.object = object;
        this.objectId = objectId;
        this.objectName = objectName;
        this.email = email;
        this.oldContent = oldContent;
        this.newContent = newContent;
        this.deleteFlg = CommonEnum.DeleteFlg.PRESERVED;
    }
    public String createDateToString(){
        return this.createdDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
