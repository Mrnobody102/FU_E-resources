package fpt.edu.eresourcessystem.repository;


import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("documentRepository")
public interface DocumentRepository extends
        MongoRepository<Document, String> {

    Optional<Document> findById(String documentId);

    List<Document> findByTopicId(String topicId);

    List<Document> findByCourseId(String courseId);


}
