package fpt.edu.eresourcessystem.model;

import fpt.edu.eresourcessystem.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("files")
public class MultiFile {
    @Id
    private String id;
    private String fileName;
    private String cloudFileLink;
    private String docId;
    private CommonEnum.DeleteFlg deleteFlg;

    public MultiFile(String fileName, String cloudFileLink) {
        this.fileName = fileName;
        this.cloudFileLink = cloudFileLink;
    }
}