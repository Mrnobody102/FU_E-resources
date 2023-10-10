package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.Topic;
import fpt.edu.eresourcessystem.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("topicService")
public class TopicServiceImpl implements TopicService{
    private final TopicRepository topicRepository;
    @Autowired
    MongoTemplate mongoTemplate;

    public TopicServiceImpl(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @Override
    public List<Topic> findAll() {
        List<Topic> topics = topicRepository.findAll();
        return topics;
    }

    @Override
    public List<Topic> findByCourseId(String courseId) {
        Query query = new Query(Criteria.where("courseId").is(courseId));
        query.fields().include("topics");
        List<Topic> topics =  mongoTemplate.find(query, Topic.class);
        return  topics;
    }

    @Override
    public void addTopic(Topic topic) {
        topicRepository.insert(topic);
    }
}
