package fpt.edu.eresourcessystem.repository;

import fpt.edu.eresourcessystem.model.CourseLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository("courseLogRepository")
public interface CourseLogRepository extends MongoRepository<CourseLog, String> {

}
