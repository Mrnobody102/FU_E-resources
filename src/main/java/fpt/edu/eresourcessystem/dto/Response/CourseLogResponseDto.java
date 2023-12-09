package fpt.edu.eresourcessystem.dto.Response;

import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.enums.CourseEnum;
import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.CourseLog;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseLogResponseDto {
    private String id;
    private String courseId;
    private String courseCode;
    private String courseName;
    private String oldContent;
    private String newContent;
    private String action;
    private String objectType;

    // Delete flag
    private String objectId;
    private String objectName;
    private CommonEnum.DeleteFlg deleteFlg;
    private String email;
    @CreatedBy
    private String createdBy;
    @CreatedDate
    private String createdDate;

    public CourseLogResponseDto(CourseLog courseLog) {
        this.id = courseLog.getId();
        this.courseId = courseLog.getCourse().getId();
        this.courseCode = courseLog.getCourse().getCourseCode();
        this.courseName = courseLog.getCourse().getCourseName();
        this.oldContent = courseLog.getOldContent();
        this.newContent = courseLog.getNewContent();
        this.action = courseLog.getAction().getDisplayValue();
        this.objectType = courseLog.getObject().getDisplayValue();
        this.objectId = courseLog.getObjectId();
        this.objectName = courseLog.getObjectName();
        this.email = courseLog.getEmail();
        this.createdBy = courseLog.getCreatedBy();
        this.createdDate = courseLog.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
