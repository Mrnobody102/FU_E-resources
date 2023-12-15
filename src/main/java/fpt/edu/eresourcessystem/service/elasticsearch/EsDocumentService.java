package fpt.edu.eresourcessystem.service.elasticsearch;

import java.util.List;
import java.util.Optional;

import fpt.edu.eresourcessystem.model.elasticsearch.EsDocument;
import fpt.edu.eresourcessystem.repository.elasticsearch.EsDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EsDocumentService {

    private final EsDocumentRepository documentRepository;
    private final ElasticsearchTemplate elasticsearchTemplate;
    private final EsDocumentRepository esDocumentRepository;

    public Iterable<EsDocument> searchDocument(String searchTerm) {
        return esDocumentRepository.search(searchTerm);
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
