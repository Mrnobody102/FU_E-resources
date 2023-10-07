package fpt.edu.eresourcessystem.model;


import fpt.edu.eresourcessystem.enums.AccountEnum;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document("accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    private String accountId;

    @Indexed(unique = true)
    private String username;

    private String password;

    @Indexed(unique = true)
    private String email;

    private String name;
    private LocalDate birthOfDate;
    private String gender;
    private String campus;

    private AccountEnum role;

    private boolean flagAdmin;

    //Audit Log
    @CreatedBy
    private String createdBy;
    @CreatedDate
    private String createdDate;
    @LastModifiedBy
    private String lastModifiedBy;
    @LastModifiedDate
    private String lastModifiedDate;
}
