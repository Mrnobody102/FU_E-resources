package fpt.edu.eresourcessystem.dto.Response;

import fpt.edu.eresourcessystem.enums.CourseEnum;
import fpt.edu.eresourcessystem.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponseDto {
    @Id
    private String id;
    private String librarian;
    private String lecturer;
    @Indexed(unique = true)
    private String courseCode;
    private String courseName;
    private String description;
    private String trainingType;
    private CourseEnum.Status status;
}