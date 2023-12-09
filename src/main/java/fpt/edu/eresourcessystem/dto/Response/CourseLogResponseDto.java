package fpt.edu.eresourcessystem.dto.Response;

import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.enums.CourseEnum;
import fpt.edu.eresourcessystem.model.Course;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

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

    // Delete flag
    private String object;
    private String objectName;
    private CommonEnum.DeleteFlg deleteFlg;
    private String email;
    @CreatedBy
    private String createdBy;
    @CreatedDate
    private String createdDate;
}
