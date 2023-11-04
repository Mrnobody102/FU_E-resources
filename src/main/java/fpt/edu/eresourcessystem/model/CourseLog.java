package fpt.edu.eresourcessystem.model;

import fpt.edu.eresourcessystem.dto.CourseLogDTO;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("course_log")
public class CourseLog {
    @Id
    private String id;

    @NotNull
    private String courseId;

    private CommonEnum.Action action;

    // Delete flag
    private CommonEnum.DeleteFlg deleteFlg;
    @CreatedBy
    private String createdBy;
    @CreatedDate
    private String createdDate;
    @LastModifiedBy
    private String lastModifiedBy;
    @LastModifiedDate
    private String lastModifiedDate;
    public CourseLog(String courseId,CommonEnum.Action action ) {
        this.courseId = courseId;
        this.action = action;
    }
    // Constructor DTO
    public CourseLog(CourseLogDTO courseLogDTO) {
        this.id = courseLogDTO.getId();
        this.courseId = courseLogDTO.getCourseId();
        this.action = courseLogDTO.getAction();
        this.createdBy = courseLogDTO.getCreatedBy();
        this.createdDate = courseLogDTO.getCreatedDate();
        this.deleteFlg = CommonEnum.DeleteFlg.PRESERVED;
    }
}
