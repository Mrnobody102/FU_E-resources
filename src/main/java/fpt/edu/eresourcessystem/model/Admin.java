package fpt.edu.eresourcessystem.model;


import fpt.edu.eresourcessystem.enums.CommonEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("admin")
public class Admin {
    @Id
    private String id;

    @NotNull
    @DocumentReference(lazy = true)
    private Account account;

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
    public Admin(@NotNull Account account) {
        this.account = account;
        this.deleteFlg = CommonEnum.DeleteFlg.PRESERVED;
    }
}
