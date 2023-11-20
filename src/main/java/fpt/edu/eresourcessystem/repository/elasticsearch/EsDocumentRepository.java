package fpt.edu.eresourcessystem.repository.elasticsearch;

import fpt.edu.eresourcessystem.model.elasticsearch.EsDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface EsDocumentRepository extends ElasticsearchRepository<EsDocument, String> {
    @Query("{\"bool\": {\"should\": [" +
            "{\"wildcard\": {\"title\": {\"value\": \"*?0*\",\"case_insensitive\": true,\"boost\": 2}}}," +
            "{\"wildcard\": {\"description\": {\"value\": \"*?0*\",\"case_insensitive\": true}}}," +
            "{\"wildcard\": {\"docType\": {\"value\": \"*?0*\",\"case_insensitive\": true}}}," +
            "{\"wildcard\": {\"editorContent\": {\"value\": \"*?0*\",\"case_insensitive\": true}}}" +
            "], \"minimum_should_match\": 1}}")
    Iterable<EsDocument> findByTitleContainingOrDescriptionContainingOrDocTypeLikeOrEditorContentContainingIgnoreCase(String search);
}