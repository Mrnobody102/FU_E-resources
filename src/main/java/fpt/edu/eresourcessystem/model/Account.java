package fpt.edu.eresourcessystem.model;


import fpt.edu.eresourcessystem.dto.AccountDTO;
import fpt.edu.eresourcessystem.enums.AccountEnum;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
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
    private String id;

    @Indexed(unique = true)
    private String username;

    private String password;

    @Indexed(unique = true)
    @NotEmpty(message = "account.validation.email.required")
    private String email;

    private String name;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfBirth;
    private AccountEnum.Gender gender;
    private AccountEnum.Campus campus;

    private AccountEnum.Role role;

    private AccountEnum.Status status;

    private AccountEnum.AccountType accountType;

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
    public Account(AccountDTO accountDTO) {
        this.id = accountDTO.getId();
        this.username = accountDTO.getUsername();
        this.password = accountDTO.getPassword();
        this.email = accountDTO.getEmail();
        this.name = accountDTO.getName();
        this.dateOfBirth = accountDTO.getDateOfBirth();
        this.gender = accountDTO.getGender();
        this.campus = accountDTO.getCampus();
        this.role = accountDTO.getRole();
        this.status = AccountEnum.Status.ACTIVE;
        this.deleteFlg = CommonEnum.DeleteFlg.PRESERVED;
    }
}
