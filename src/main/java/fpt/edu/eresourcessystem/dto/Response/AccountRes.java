package fpt.edu.eresourcessystem.dto.Response;

import fpt.edu.eresourcessystem.enums.AccountEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountRes {
    private String id;
    private String username;
    private String passwordCrypt;
    private String email;
    private String name;
    private LocalDate dateOfBirth;
    private AccountEnum.Gender gender;
    private AccountEnum.Campus campus;
    private AccountEnum.Role role;
    private AccountEnum.Status status;
    private String createdBy;
    private String createdDate;
    private String lastModifiedBy;
    private String lastModifiedDate;
}
