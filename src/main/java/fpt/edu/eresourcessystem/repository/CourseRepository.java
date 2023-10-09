package fpt.edu.eresourcessystem.repository;

import fpt.edu.eresourcessystem.model.Course;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends
        MongoRepository<Course, String> {

    Optional<Course> findById(String courseId);
    // nên chỉ định rõ kq trả về

    Course findByCourseCode(String courseCode);
}
