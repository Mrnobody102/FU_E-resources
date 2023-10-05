package fpt.edu.eresourcessystem.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("accounts")
public class Student {
    private String studentId;  // Reference to Account
    //Reference by String ID, increase performance
    private List<String> enrolledCourses;
    private List<String> savedDocuments;

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
