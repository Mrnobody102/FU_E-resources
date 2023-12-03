package fpt.edu.eresourcessystem.repository.elasticsearch;

import fpt.edu.eresourcessystem.model.elasticsearch.EsDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EsDocumentRepository extends ElasticsearchRepository<EsDocument, String> {
    @Query("{\"bool\": {\"should\": [{\"match_phrase\": {\"title\": \"*?0*\"}}," +
            "{\"match_phrase\": {\"description\": \"*?0*\"}}," +
            "{\"match_phrase\": {\"docType\": \"*?0*\"}}," +
            "{\"match_phrase\": {\"content\": \"*?0*\"}}," +
            "{\"match\": {\"title\": \"*?0*\"}}," +
            "{\"match\": {\"description\": \"*?0*\"}}," +
            "{\"match\": {\"content\": \"*?0*\"}}," +
            "{\"regexp\": {\"title\": \".*?0.*\"}}," +
            "{\"regexp\": {\"description\": \".*?0.*\"}}," +
            "{\"regexp\": {\"content\": \".*?0.*\"}}]}}")
    Iterable<EsDocument> search(String search);
    default Iterable<EsDocument> findBySearchTerm(String searchTerm) {
        String cleanedSearchTerm = searchTerm.toLowerCase().trim();
        return search(cleanedSearchTerm);
    }
}