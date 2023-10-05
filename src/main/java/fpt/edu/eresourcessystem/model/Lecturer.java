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
public class Lecturer {
    private String lecturerId;  // Reference to Account
    private List<String> lecturerCourses;

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
