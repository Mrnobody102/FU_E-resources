package fpt.edu.eresourcessystem.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document("students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Student {
    @Id
    private String studentId;
    @NotNull
    private String accountId;

    private List<String> savedDocuments;
    private List<String> savedCourses;
    private List<String> notes;
    private List<String> notifications;
    private List<String> requests;
    private List<String> feedbacks;

    //Audit Log
    @CreatedBy
    @Field("studentCreatedBy")
    private String createdBy;
    @CreatedDate
    @Field("studentCreatedDate")
    private String createdDate;
    @LastModifiedBy
    @Field("studentLastModifiedBy")
    private String lastModifiedBy;
    @LastModifiedDate
    @Field("studentLastModifiedDate")
    private String lastModifiedDate;
}
