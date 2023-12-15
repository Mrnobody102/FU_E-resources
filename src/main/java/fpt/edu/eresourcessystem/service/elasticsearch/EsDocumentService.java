package fpt.edu.eresourcessystem.service.elasticsearch;

import fpt.edu.eresourcessystem.model.elasticsearch.EsDocument;
import fpt.edu.eresourcessystem.repository.elasticsearch.EsDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class EsDocumentService {

    private final EsDocumentRepository documentRepository;
    private final EsDocumentRepository esDocumentRepository;

    public Page<EsDocument> searchDocument(String searchTerm, int skip) {
        Pageable pageable = PageRequest.of(skip, 10);
        return esDocumentRepository.search(searchTerm, pageable);
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
