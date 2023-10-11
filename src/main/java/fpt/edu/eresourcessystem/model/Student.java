package fpt.edu.eresourcessystem.model;

import com.mongodb.lang.NonNull;
import lombok.Data;
import lombok.AllArgsConstructor;
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
    private String studentId;  // Reference to Account
    //Reference by String ID, increase performance

    @NonNull
    private String accountId;

    private List<String> enrolledCourses;
    private List<String> savedDocuments;
    private List<String> savedCourses;

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
