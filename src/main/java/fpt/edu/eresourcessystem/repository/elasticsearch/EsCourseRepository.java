package fpt.edu.eresourcessystem.repository.elasticsearch;

import fpt.edu.eresourcessystem.model.elasticsearch.EsCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EsCourseRepository extends ElasticsearchRepository<EsCourse, String> {
    @Query("{\"bool\": " +
            "{\"should\": " +
            "[{\"match_phrase\": {\"code\": \"*?0*\"}}," +
            "{\"match_phrase\": {\"name\": \"*?0*\"}}," +
            "{\"match_phrase\": {\"lecturer\": \"*?0*\"}}," +
            "{\"match_phrase\": {\"description\": \"*?0*\"}}," +
            "{\"match\": {\"code\": \"*?0*\"}}," +
            "{\"match\": {\"name\": \"*?0*\"}}," +
            "{\"match\": {\"lecturer\": \"*?0*\"}}," +
            "{\"match\": {\"description\": \"*?0*\"}}," +
            "{\"regexp\": {\"code\": \".*?0.*\"}}," +
            "{\"regexp\": {\"name\": \".*?0.*\"}}," +
            "{\"regexp\": {\"lecturer\": \".*?0.*\"}}," +
            "{\"regexp\": {\"description\": \".*?0.*\"}}]" +
            "}}")
    Page<EsCourse> search(String search, Pageable pageable);

    @Query("{\"bool\": {\"should\": [" +
            "{\"match_phrase\": {\"courseId.keyword\": \"?0\"}}, " +
            "{\"match_phrase\": {\"code.keyword\": \"?0\"}}, " +
            "{\"match_phrase\": {\"name.keyword\": \"?0\"}}, " +
            "{\"match_phrase\": {\"description.keyword\": \"?0\"}}" +
            "]}}")
    SearchPage<EsCourse> customSearch(String search, Pageable pageable);
}