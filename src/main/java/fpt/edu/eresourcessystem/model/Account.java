package fpt.edu.eresourcessystem.model;


import fpt.edu.eresourcessystem.enums.AccountEnum;
import jakarta.annotation.Nonnull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document("accounts")
@Getter
@Setter
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

    private AccountEnum.Role role;

    private boolean flagAdmin;
}
