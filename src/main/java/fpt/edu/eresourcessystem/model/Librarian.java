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


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("accounts")
public class Librarian extends Account{
    private String librarianId;  // Reference to Account

    //Audit Log
    @CreatedBy
    @Field("librarianCreatedBy")
    private String createdBy;
    @CreatedDate
    @Field("librarianCreatedDate")
    private String createdDate;
    @LastModifiedBy
    @Field("librarianLastModifiedBy")
    private String lastModifiedBy;
    @LastModifiedDate
    @Field("librarianLastModifiedDate")
    private String lastModifiedDate;
}
