package fpt.edu.eresourcessystem.dto;

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
public class AccountDTO {

    @NotNull
    private String accountId;

    @NotEmpty()
    private String username;

    @NotEmpty()
    private String password;

    @Email
    @NotEmpty(message = "account.validation.email.required")
    private String email;

    private String name;
    private LocalDate dateOfBirth;
    private AccountEnum.Gender gender;

    @NotEmpty(message = "account.validation.campus.required")
    private AccountEnum.Campus campus;

    @NotNull(message = "account.validation.role.required")
    private AccountEnum.Role role;
}
