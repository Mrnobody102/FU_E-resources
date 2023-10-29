package fpt.edu.eresourcessystem.model;


import fpt.edu.eresourcessystem.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("lecturer_course")
public class LecturerCourse {
    @Id
    private LecturerCourseId id;

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
}
