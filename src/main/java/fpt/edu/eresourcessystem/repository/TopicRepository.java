package fpt.edu.eresourcessystem.repository;

import fpt.edu.eresourcessystem.model.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("topicRepository")
public interface TopicRepository extends MongoRepository<Topic,String> {

    @Override
    List<Topic> findAll();

    Optional<Topic> findById(String topicId);
}
