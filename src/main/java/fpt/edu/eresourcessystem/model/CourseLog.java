package fpt.edu.eresourcessystem.model;

import fpt.edu.eresourcessystem.dto.CourseLogDTO;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("course_log")
public class CourseLog {
    @Id
    private String id;
    @DocumentReference(lazy = true)
    private Course course;
    private String oldContent;
    private String newContent;
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
    public CourseLog(Course course,CommonEnum.Action action ) {
        this.course = course;
        this.action = action;
        this.deleteFlg = CommonEnum.DeleteFlg.PRESERVED;
    }
    // Constructor DTO
    public CourseLog(CourseLogDTO courseLogDTO) {
        this.id = courseLogDTO.getId();
        this.course = courseLogDTO.getCourse();
        this.action = courseLogDTO.getAction();
        this.createdBy = courseLogDTO.getCreatedBy();
        this.createdDate = courseLogDTO.getCreatedDate();
        this.oldContent = courseLogDTO.getOldContent();
        this.newContent = courseLogDTO.getNewContent();
        if(null==courseLogDTO.getAction()){
            this.action = CommonEnum.Action.VIEW;
        }else{
            this.action = courseLogDTO.getAction();
        }
        this.deleteFlg = CommonEnum.DeleteFlg.PRESERVED;
    }
}
