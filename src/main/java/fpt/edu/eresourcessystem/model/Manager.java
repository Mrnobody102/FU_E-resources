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
public class Manager extends Account{
    private String managerId;  // Reference to Account

    //Audit Log
    @CreatedBy
    @Field("managerCreatedBy")
    private String createdBy;
    @CreatedDate
    @Field("managerCreatedDate")
    private String createdDate;
    @LastModifiedBy
    @Field("managerLastModifiedBy")
    private String lastModifiedBy;
    @LastModifiedDate
    @Field("managerLastModifiedDate")
    private String lastModifiedDate;
}
