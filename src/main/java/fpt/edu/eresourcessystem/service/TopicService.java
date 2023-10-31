package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.Topic;

import java.util.List;

public interface TopicService {
    List<Topic> findAll();

    Topic addTopic(Topic topic);

    Topic findById(String topicId);

    Topic updateTopic(Topic topic);

    boolean delete(String topicId);

    Topic addDocument(Topic topic);
}
