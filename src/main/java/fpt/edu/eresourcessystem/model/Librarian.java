package fpt.edu.eresourcessystem.model;


import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("librarians")
public class Librarian{
    @Id
    private String librarianId;
    @NotNull
    private String accountId;

    private boolean flagAdmin;

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
