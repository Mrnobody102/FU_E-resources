package fpt.edu.eresourcessystem.dto.Response;

import fpt.edu.eresourcessystem.model.Lecturer;
import fpt.edu.eresourcessystem.model.LecturerCourse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LecturerCourseResponseDto {
    private String lecturerId;
    private String lecturerName;
    private String lecturerEmail;
    private String courseId;

    private String startDate;
    private String endDate;

    public LecturerCourseResponseDto(Lecturer lecturer, LecturerCourse lecturerCourse){
        this.lecturerId = lecturer.getId();
        this.lecturerName = lecturer.getAccount().getName();
        this.lecturerEmail = lecturer.getAccount().getEmail();
        this.courseId = lecturerCourse.getId().getCourseId();
        this.startDate = lecturerCourse.getId().getCreatedDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        this.endDate = lecturerCourse.getId().getLastModifiedDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

}
