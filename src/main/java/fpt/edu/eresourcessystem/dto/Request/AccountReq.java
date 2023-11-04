package fpt.edu.eresourcessystem.dto.Request;

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
public class AccountReq {

    // In case login with FPT mail, only request this field
    @Email
    @NotEmpty(message = "account.validation.email.required")
    private String email;

    @NotEmpty()
    private String username;
    @NotEmpty()
    private String password;
    private String name;
    private LocalDate dateOfBirth;
    private AccountEnum.Gender gender;
    private AccountEnum.Campus campus;

    @NotNull(message = "account.validation.role.required")
    private AccountEnum.Role role;
}
