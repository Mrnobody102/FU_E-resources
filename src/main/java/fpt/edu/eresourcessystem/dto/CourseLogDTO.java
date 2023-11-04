package fpt.edu.eresourcessystem.dto;

import fpt.edu.eresourcessystem.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CourseLogDTO {
    private String id;

    private String courseId;

    private CommonEnum.Action action;
    // Delete flag
    private CommonEnum.DeleteFlg deleteFlg;

    private String createdBy;

    private String createdDate;

    private String lastModifiedBy;

    private String lastModifiedDate;
}
