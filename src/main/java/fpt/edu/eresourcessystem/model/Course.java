package fpt.edu.eresourcessystem.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document("courses")
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    private String courseId;

    @Indexed(unique = true)
    private String courseCode;

    private String courseName;
    private String description;

    private List<String> documents; //list of documentId
    private List<String> folders; //list of folderId
    private List<Topic> topics;
    private List<Lecturer> lecturers;
    //Audit Log
    @CreatedBy
    private String createdBy;
    @CreatedDate
    private String createdDate;
    @LastModifiedBy
    private String lastModifiedBy;
    @LastModifiedDate
    private String lastModifiedDate;


    public boolean updateTopic(Topic topic){
        if(null != this.topics ){
            for (int i=0; i<topics.size();i++) {
                if(topics.get(i).getTopicId().equals(topic.getTopicId())){
                    topics.set(i, topic);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean deleteTopic(Topic topic){
        if(null != this.topics ){
            for (int i=0; i<topics.size();i++) {
                if(topics.get(i).getTopicId().equals(topic.getTopicId())){
                    topics.remove(i);
                    return true;
                }
            }
        }
        return false;
    }
}