package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Topic;
import fpt.edu.eresourcessystem.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        List<Topic> topics =  mongoTemplate.find(query, Topic.class);
        return  topics;
    }

    @Override
    public Topic addTopic(Topic topic) {
        if(null==topic.getTopicId()) {
            Topic result = topicRepository.save(topic);
            return result;
        }else{
            Optional<Topic> checkExist = topicRepository.findById(topic.getTopicId());
            if(!checkExist.isPresent()){
                Topic result = topicRepository.save(topic);
                return result;
            }return null;
        }
    }

    @Override
    public Topic findById(String topicId) {
        Optional<Topic> topic = topicRepository.findById(topicId);
        return topic.orElse(null);
    }

    @Override
    public Topic updateTopic(Topic topic) {
        Optional<Topic> checkExist = topicRepository.findById(topic.getTopicId());
       if(checkExist.isPresent()){
           Topic result = topicRepository.save(topic);
           return result;
       }
       return null;
    }

    @Override
    public boolean delete(String topicId) {
        Optional<Topic> check = topicRepository.findById(topicId);
        if(check.isPresent()){
            topicRepository.deleteById(topicId);
            return true;
        }
        return false;
    }
}
