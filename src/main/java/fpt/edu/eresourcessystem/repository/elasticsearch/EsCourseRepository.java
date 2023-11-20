package fpt.edu.eresourcessystem.repository.elasticsearch;

import fpt.edu.eresourcessystem.model.elasticsearch.EsCourse;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EsCourseRepository extends ElasticsearchRepository<EsCourse, String> {

}