package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Document;
import fpt.edu.eresourcessystem.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.List;
import java.util.Optional;

@Service("documentService")
public class DocumentServiceImpl implements DocumentService{
    private final DocumentRepository documentRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public Document findById(String documentId) {
        Optional<Document> document = documentRepository.findById(documentId);
        return document.orElse(null);
    }

    @Override
    public List<Document> findByTopicId(String topicId) {
        List<Document> documents = documentRepository.findByTopicId(topicId);
        return documents;
    }

    @Override
    public List<Document> findByCourseId(String courseId) {
        List<Document> documents = documentRepository.findByCourseId(courseId);
        return documents;
    }

    @Override
    public Document addDocument(Document document) {
        if(null==document.getDocumentId()){
            Document result = documentRepository.save(document);
            return result;
        }return null;
    }
}
