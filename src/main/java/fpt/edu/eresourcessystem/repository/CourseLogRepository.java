package fpt.edu.eresourcessystem.repository;

import fpt.edu.eresourcessystem.model.CourseLog;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("courseLogRepository")
public interface CourseLogRepository extends MongoRepository<CourseLog, String> {
    List<CourseLog> findByEmail(String courseId);
    List<CourseLog> findAll(Sort sort);
}
