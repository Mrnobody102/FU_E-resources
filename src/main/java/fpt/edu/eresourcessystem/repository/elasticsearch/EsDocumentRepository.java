package fpt.edu.eresourcessystem.repository.elasticsearch;

import fpt.edu.eresourcessystem.model.elasticsearch.EsDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EsDocumentRepository extends ElasticsearchRepository<EsDocument, String> {

    @Query("{\"bool\": {\"should\": [{\"wildcard\": {\"title\": \"*?0*\"}}, {\"wildcard\": {\"description\": \"*?0*\"}}, {\"wildcard\": {\"docType\": \"*?0*\"}}]}}")
    Iterable<EsDocument> findByTitleContainingOrDescriptionContainingOrDocTypeLikeIgnoreCase(String search);

    default Iterable<EsDocument> findBySearchTerm(String searchTerm) {
        String cleanedSearchTerm = searchTerm.toLowerCase().trim();
        return findByTitleContainingOrDescriptionContainingOrDocTypeLikeIgnoreCase(cleanedSearchTerm);
    }
}