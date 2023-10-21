package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Document;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DocumentService {
    List<Document> findAll();

    Page<Document> filterAndSearchDocument(String course, String topic, String title, String description, int pageIndex, int pageSize);

    Document addDocument(Document document, String id) throws IOException;

    Document findById(String documentId);

    Document updateDocument(Document document) throws IOException;

    boolean delete(String documentId);
    List<Document> findDocumentsByTopicId(String topicId);

    String addFile(MultipartFile file) throws IOException;
}
