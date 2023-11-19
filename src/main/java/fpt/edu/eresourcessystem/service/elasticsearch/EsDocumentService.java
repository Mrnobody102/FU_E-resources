package fpt.edu.eresourcessystem.service.elasticsearch;

import java.util.List;
import java.util.Optional;

import fpt.edu.eresourcessystem.model.elasticsearch.EsDocument;
import fpt.edu.eresourcessystem.repository.elasticsearch.EsDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EsDocumentService {

    private final EsDocumentRepository documentRepository;

    @Autowired
    public EsDocumentService(EsDocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public EsDocument save(EsDocument document) {
        return documentRepository.save(document);
    }

    public void delete(EsDocument document) {
        documentRepository.delete(document);
    }

    public Optional<EsDocument> findOne(String id) {
        return documentRepository.findById(id);
    }

    public Iterable<EsDocument> findAll() {
        return documentRepository.findAll();
    }
}
