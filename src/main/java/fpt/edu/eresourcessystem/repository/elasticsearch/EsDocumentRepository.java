package fpt.edu.eresourcessystem.repository.elasticsearch;

import fpt.edu.eresourcessystem.model.elasticsearch.EsDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface EsDocumentRepository extends ElasticsearchRepository<EsDocument, String> {
    @Query("{\"bool\": {\"should\": [" +
            "{\"match_phrase\": {\"title\": {\"value\": \"*?0*\",\"case_insensitive\": true,\"boost\": 2}}}," +
            "{\"match_phrase\": {\"description\": {\"value\": \"*?0*\",\"case_insensitive\": true}}}," +
            "{\"match_phrase\": {\"docType\": {\"value\": \"*?0*\",\"case_insensitive\": true}}}," +
            "{\"match_phrase\": {\"editorContent\": {\"value\": \"*?0*\",\"case_insensitive\": true}}}" +
            "], \"minimum_should_match\": 1}}")
    Iterable<EsDocument> findByTitleContainingOrDescriptionContainingOrDocTypeLikeOrEditorContentContainingIgnoreCase(String search);
}