package fpt.edu.eresourcessystem.dto;


import fpt.edu.eresourcessystem.enums.AccountEnum;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLogDto {
    private String id;

    private String url;

    // Delete flag
    private CommonEnum.DeleteFlg deleteFlg;

    private String createdBy; // email

    private LocalDateTime date; // time


    private AccountEnum.Role role;
    private String email;

    public UserLogDto(String url, String email , AccountEnum.Role role) {
        this.url = url;
        this.role = role;
        this.email = email;
    }
}
