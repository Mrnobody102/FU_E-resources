package fpt.edu.eresourcessystem.repository;

import fpt.edu.eresourcessystem.model.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("topicRepository")
public interface TopicRepository extends MongoRepository<Topic,String> {

    @Override
    List<Topic> findAll();

}
