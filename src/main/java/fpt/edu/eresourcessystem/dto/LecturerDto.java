package fpt.edu.eresourcessystem.dto.Response;

import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.Lecturer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LecturerDto {
    @Id
    private String id;
    private String accountName;
    private String accountEmail;
    private List<String> courses; // Assuming these are course titles or IDs
    private int totalCourses;
    private String lastModifiedDate;

    public LecturerDto(Lecturer lecturer) {
        this.id = lecturer.getId();
        if(lecturer.getAccount() != null) {
            this.accountName = lecturer.getAccount().getName();
            this.accountEmail = lecturer.getAccount().getEmail();
        }
        this.courses = lecturer.getCourses() != null
                ? lecturer.getCourses().stream().map(Course::getCourseCode).collect(Collectors.toList())
                : Collections.emptyList();

        if(lecturer.getCourses() != null) {
            this.totalCourses = lecturer.getCourses().size();
        }
        this.lastModifiedDate = lecturer.getLastModifiedDate();
    }
}
