package fpt.edu.eresourcessystem.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.util.Objects;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LecturerCourseId {
    @NotNull
    private String lecturerId;

    @NotNull
    private String courseId;

    private LocalDate createdDate;

    private LocalDate lastModifiedDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LecturerCourseId that = (LecturerCourseId) o;
        return Objects.equals(lecturerId, that.lecturerId) && Objects.equals(createdDate, that.createdDate)
                && Objects.equals(lastModifiedDate, that.lastModifiedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lecturerId, createdDate, lastModifiedDate);
    }
}
