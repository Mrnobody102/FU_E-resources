package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Document;
import fpt.edu.eresourcessystem.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("documentService")
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    @Autowired
    MongoTemplate mongoTemplate;

    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public List<Document> findAll() {
        List<Document> documents = documentRepository.findAll();
        return documents;
    }

    @Override
    public List<Document> findByCourseId(String courseId) {
        Query query = new Query(Criteria.where("courseId").is(courseId));
        List<Document> documents = mongoTemplate.find(query, Document.class);
        return documents;
    }

    @Override
    public Document addDocument(Document document) {
        if (null == document.getDocumentId()) {
            Document result = documentRepository.save(document);
            return result;
        } else {
            Optional<Document> checkExist = documentRepository.findById(document.getDocumentId());
            if (!checkExist.isPresent()) {
                Document result = documentRepository.save(document);
                return result;
            }
            return null;
        }
    }

    @Override
    public Document findById(String documentId) {
        Optional<Document> document = documentRepository.findById(documentId);
        return document.orElse(null);
    }

    @Override
    public Document updateDocument(Document document) {
        Optional<Document> checkExist = documentRepository.findById(document.getDocumentId());
        if (checkExist.isPresent()) {
            Document result = documentRepository.save(document);
            return result;
        }
        return null;
    }

    @Override
    public boolean delete(String documentId) {
        Optional<Document> check = documentRepository.findById(documentId);
        if (check.isPresent()) {
            documentRepository.deleteById(documentId);
            return true;
        }
        return false;
    }

    @Override
    public List<Document> findByTopicId(String topicId) {
        List<Document> documents = documentRepository.findByTopicId(topicId);
        return documents;
    }
}
