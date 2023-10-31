package fpt.edu.eresourcessystem.model;

import fpt.edu.eresourcessystem.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseLogId {
    private String accountId;
    private String courseId;
    private CommonEnum.Action action;
    private LocalDateTime time;
}
