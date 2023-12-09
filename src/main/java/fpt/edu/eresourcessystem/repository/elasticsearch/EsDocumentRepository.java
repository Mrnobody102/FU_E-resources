package fpt.edu.eresourcessystem.repository.elasticsearch;

import org.elasticsearch.index.query.QueryBuilders;
import fpt.edu.eresourcessystem.model.elasticsearch.EsDocument;
import org.elasticsearch.index.query.BoolQueryBuilder;
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
        String[] words = searchTerm.toLowerCase().trim().split("\\s+");
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        for (String word : words) {
            queryBuilder.should(QueryBuilders.matchQuery("title", word))
                    .should(QueryBuilders.matchQuery("description", word))
                    .should(QueryBuilders.matchQuery("content", word));
        }
        return search(queryBuilder.toString());
    }




//    default Iterable<EsDocument> findBySearchTerm(String searchTerm) {
//        String cleanedSearchTerm = searchTerm.toLowerCase().trim();
//        String[] words = cleanedSearchTerm.split("\\s+");
//
//        StringBuilder queryBuilder = new StringBuilder("{\"bool\": {\"should\": [");
//
//        for (String word : words) {
//            queryBuilder.append("{\"match\": {\"title\": \"").append(word).append("\"}},")
//                    .append("{\"match\": {\"description\": \"").append(word).append("\"}},")
//                    .append("{\"match\": {\"content\": \"").append(word).append("\"}},");
//        }
//
//        queryBuilder.deleteCharAt(queryBuilder.length() - 1); // Xóa dấu phẩy cuối cùng
//        queryBuilder.append("]}}");
//
//        return search(queryBuilder.toString());
//    }
}