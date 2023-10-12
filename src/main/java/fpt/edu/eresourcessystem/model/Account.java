package fpt.edu.eresourcessystem.model;


import fpt.edu.eresourcessystem.enums.AccountEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Document("accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Account {
    @Id
    private String accountId;

    @Indexed(unique = true)
    private String username;

    private String password;

    @Indexed(unique = true)
    @NotNull(message = "{email.not.null}")
    private String email;

    private String name;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfBirth;
    private AccountEnum.Gender gender;
    private AccountEnum.Campus campus;

    private AccountEnum.Role role;

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
