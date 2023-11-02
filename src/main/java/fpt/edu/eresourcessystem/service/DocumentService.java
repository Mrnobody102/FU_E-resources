package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.dto.DocumentDTO;
import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.Document;
import fpt.edu.eresourcessystem.model.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DocumentService {
    List<Document> findAll();

    Page<Document> filterAndSearchDocument(String course, String topic, String title, String description, int pageIndex, int pageSize);

    Document addDocument(DocumentDTO documentDTO, String id) throws IOException;

    Document findById(String documentId);
    List<Document> findByListId(List<String> documentIds);

    Document updateDocument(Document document) throws IOException;

    boolean softDelete(Document document);

    boolean delete(String documentId);
    String addFile(MultipartFile file) throws IOException;
}
