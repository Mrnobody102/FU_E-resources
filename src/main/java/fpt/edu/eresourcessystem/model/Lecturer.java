package fpt.edu.eresourcessystem.model;


import fpt.edu.eresourcessystem.enums.CommonEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("lecturers")
public class Lecturer {
    @Id
    private String id;

    @NotNull
    @DocumentReference
    private Account account;

    @DocumentReference
    private TrainingType trainingType;

    private List<String> lecturerCourses;
    @DocumentReference(lazy = true)
    private List<Course> courses;
    private List<String> documents;
    private List<String> answers; // 1 answer per question

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

    // Constructor
    public Lecturer(@NotNull Account account) {
        this.account = account;
        this.deleteFlg = CommonEnum.DeleteFlg.PRESERVED;
    }
}
