package fpt.edu.eresourcessystem.model;


import fpt.edu.eresourcessystem.dto.TopicDTO;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Data
@Document("topics")
@NoArgsConstructor
@AllArgsConstructor
public class Topic {
    @Id
    private String id;
    @NotNull
    @DocumentReference(lazy = true)
    private Course course;
    @DocumentReference(lazy = true)
    private List<ResourceType> resourceTypes; // NEW

    private String topicTitle;
    private String topicDescription;
    @DocumentReference(lazy = true)
    private List<fpt.edu.eresourcessystem.model.Document> documents;

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
    public Topic(TopicDTO topicDTO) {
        this.id = topicDTO.getId();
        this.course = topicDTO.getCourse();
        this.topicTitle = topicDTO.getTopicTitle();
        this.topicDescription = topicDTO.getTopicDescription();
        this.deleteFlg = CommonEnum.DeleteFlg.PRESERVED;
    }
}
