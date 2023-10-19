package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Document;

import java.util.List;

public interface DocumentService {
    List<Document> findAll();

    List<Document> findByCourseId(String courseId);

    Document addDocument(Document document);

    Document findById(String documentId);

    Document updateDocument(Document document);

    boolean delete(String documentId);
    List<Document> findByTopicId(String topicId);
}
