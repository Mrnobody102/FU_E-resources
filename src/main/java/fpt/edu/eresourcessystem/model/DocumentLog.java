package fpt.edu.eresourcessystem.model;

import fpt.edu.eresourcessystem.enums.CommonEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("document_logs")
public class DocumentLog {
    @Id
    private String id;

    @NotNull
    private String documentId;

    private CommonEnum.Action action;

    // Delete flag
    private CommonEnum.DeleteFlg deleteFlg;
    @CreatedBy
    private String account;
    @CreatedDate
    private String date;
}
