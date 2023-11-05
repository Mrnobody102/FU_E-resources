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
@Document("user_logs")
public class UserLog {
    @Id
    private String id;

    @NotNull
    private String url;

    // Delete flag
    private CommonEnum.DeleteFlg deleteFlg;

    @CreatedBy
    private String account; // email
    @CreatedDate
    private String date; // time

}
