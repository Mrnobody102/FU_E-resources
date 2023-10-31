package fpt.edu.eresourcessystem.repository;

import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.CourseLog;
import fpt.edu.eresourcessystem.model.CourseLogId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("courseLogRepository")
public interface CourseLogRepository extends MongoRepository<CourseLog, CourseLogId> {

}
