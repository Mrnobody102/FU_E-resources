package fpt.edu.eresourcessystem.dto;


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

    private String account; // email

    private LocalDateTime date; // time

    

    public UserLogDto(String url) {
        this.url = url;
    }
}
