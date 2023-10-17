package fpt.edu.eresourcessystem.model;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("lecturers")
public class Lecturer {
    @Id
    private String lecturerId;
    @NotNull
    private String accountId;

    private List<String> lecturerCourses;
//    private List<String> uploadPermission;
    private List<String> documents;
    private List<String> answers; // 1 answer per question

    //Audit Log
    @CreatedBy
    @Field("lecturerCreatedBy")
    private String createdBy;
    @CreatedDate
    @Field("lecturerCreatedDate")
    private String createdDate;
    @LastModifiedBy
    @Field("lecturerLastModifiedBy")
    private String lastModifiedBy;
    @LastModifiedDate
    @Field("lecturerLastModifiedDate")
    private String lastModifiedDate;

}
