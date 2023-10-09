package fpt.edu.eresourcessystem.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document("lessons")
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    @Id
    private Long lessonId;

    @NonNull
    private String lessonTitle;

    private String courseId;

    private String lessonDescription;

    @CreatedBy
    private String createdBy;
    @CreatedDate
    private String createdDate;
    @LastModifiedBy
    private String lastModifiedBy;
    @LastModifiedDate
    private String lastModifiedDate;
}
