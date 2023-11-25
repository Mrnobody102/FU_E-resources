package fpt.edu.eresourcessystem.model;

import fpt.edu.eresourcessystem.dto.UserLogDto;
import fpt.edu.eresourcessystem.enums.AccountEnum;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("user_logs")
public class UserLog {
    @Id
    private String id;

    @NotNull
    private String url;

    // Delete flag
    private CommonEnum.DeleteFlg deleteFlg;

    @CreatedBy
    private String account; // email
    @CreatedDate
    private LocalDateTime time; // time
    private AccountEnum.Role role;
    public UserLog(UserLogDto userLogDto){
        this.id = userLogDto.getId();
        this.url = userLogDto.getUrl();
        this.deleteFlg = CommonEnum.DeleteFlg.PRESERVED;
    }
}
