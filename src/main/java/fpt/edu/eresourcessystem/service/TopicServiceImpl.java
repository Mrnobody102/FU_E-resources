package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.model.Document;
import fpt.edu.eresourcessystem.model.Topic;
import fpt.edu.eresourcessystem.repository.TopicRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("topicService")
public class TopicServiceImpl implements TopicService{
    private final TopicRepository topicRepository;
    private final DocumentService documentService;
    MongoTemplate mongoTemplate;

    public TopicServiceImpl(TopicRepository topicRepository, DocumentService documentService, MongoTemplate mongoTemplate) {
        this.topicRepository = topicRepository;
        this.documentService = documentService;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Topic> findAll() {
        List<Topic> topics = topicRepository.findAll();
        return topics;
    }

    @Override
    public Topic addTopic(Topic topic) {
        if(null==topic.getId()) {
            Topic result = topicRepository.save(topic);
            return result;
        }else{
            Optional<Topic> checkExist = topicRepository.findById(topic.getId());
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
        Optional<Topic> checkExist = topicRepository.findById(topic.getId());
       if(checkExist.isPresent()){
           Topic result = topicRepository.save(topic);
           return result;
       }
       return null;
    }

    @Override
    public boolean softDelete(Topic topic) {
        Optional<Topic> check = topicRepository.findById(topic.getId());
        if(check.isPresent()){
            // Soft delete document first
            for(Document document:topic.getDocuments()) {
                documentService.softDelete(document);
            }
            // Soft delete topic
            topic.setDeleteFlg(CommonEnum.DeleteFlg.DELETED);
            topicRepository.save(topic);
            return true;
        }
        return false;
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
