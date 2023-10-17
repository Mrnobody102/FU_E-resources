package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Document;

import java.util.List;
import java.util.Optional;

public interface DocumentService {


    Document findById(String documentId);

    List<Document> findByTopicId(String topicId);

    List<Document> findByCourseId(String courseId);

    Document addDocument(Document document);

}
