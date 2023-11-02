package fpt.edu.eresourcessystem.repository;

import com.mongodb.lang.NonNull;
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
    Optional<Document> findById(String id);

    @Query("{$and: ["
            + "{$or: ["
            + "    {id: {$regex: ?1}},"
            + "    {id: null},"
            + "    {id: ''}"
            + "    ]},"
            + "{$or: ["
            + "    {title: {$regex: ?2}},"
            + "    {title: null},"
            + "    {title: ''}"
            + "    ]},"
            + "{$or: ["
            + "    {description: {$regex: ?3}},"
            + "    {description: null},"
            + "    {description: ''}"
            + "    ]}"
            + "]}")
    Page<Document> filterAndSearchDocument(String course, String topic, String title, String description,
                                         Pageable pageable);
}
