package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.dto.DocumentDto;
import fpt.edu.eresourcessystem.dto.Response.DocumentResponseDto;
import fpt.edu.eresourcessystem.model.Document;
import fpt.edu.eresourcessystem.model.Lecturer;
import fpt.edu.eresourcessystem.model.elasticsearch.EsDocument;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DocumentService {
    List<Document> findAll();

    List<Document> findByLecturer(Lecturer lecturer);

    Page<Document> filterAndSearchDocument(String course, String topic, String title, String description, int pageIndex, int pageSize);

    Iterable<EsDocument> searchDocument(String search);

    Document addDocument(DocumentDto documentDTO, String id) throws IOException;

    Document findById(String documentId);
    List<Document> findByListId(List<String> documentIds);

    Document updateDocument(Document document) throws IOException;

    boolean softDelete(Document document);

    boolean delete(String documentId);
    String addFile(MultipartFile file) throws IOException;

    List<DocumentResponseDto> findRelevantDocument(String topicId, String docId);
}
