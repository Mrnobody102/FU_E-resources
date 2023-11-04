package fpt.edu.eresourcessystem.model;


import fpt.edu.eresourcessystem.enums.CommonEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("rates")
public class Rate {
    @Id
    private String id;
    @NotNull
    private String studentId;
    @NotNull
    private String documentId;

    // 1 - 5
    private Integer rate;

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

    // Constructor DTO

    public Rate(String id, @NotNull String studentId, @NotNull String documentId, Integer rate) {
        this.id = id;
        this.studentId = studentId;
        this.documentId = documentId;
        this.rate = rate;
    }
}
