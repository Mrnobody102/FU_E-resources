package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.Topic;

import java.util.List;

public interface TopicService {
    List<Topic> findAll();

    List<Topic> findByCourseId(String courseId);

    void addTopic(Topic topic);
}
