package fpt.edu.eresourcessystem.dto;

import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.Feedback;
import fpt.edu.eresourcessystem.model.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    @Id
    private String id;
    private String questionId;
    private String answerId;
    private String feedbackId;
    private String from;
    private String type;
    private String to;
    private String link;

    // Notification related objects
    private Course course;
    private fpt.edu.eresourcessystem.model.Document document;
    private Feedback feedback;
    private Question question;
}