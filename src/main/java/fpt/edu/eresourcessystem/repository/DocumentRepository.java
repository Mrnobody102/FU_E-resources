package fpt.edu.eresourcessystem.repository;

import fpt.edu.eresourcessystem.model.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("documentRepository")
public interface DocumentRepository extends
        MongoRepository<Document, String> {

    Optional<Document> findById(String courseId);

    @Query("SELECT c FROM Documents c WHERE c.courseId in ?1")
    List<Document> findByListId(List<String> courseId);
}
