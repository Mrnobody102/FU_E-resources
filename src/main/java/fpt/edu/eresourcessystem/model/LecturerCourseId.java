package fpt.edu.eresourcessystem.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;


@Getter
@Setter
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
