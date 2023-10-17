package fpt.edu.eresourcessystem.repository;

import fpt.edu.eresourcessystem.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("courseRepository")
public interface CourseRepository extends
        MongoRepository<Course, String> {

    Optional<Course> findById(String courseId);

    Course findByCourseCode(String courseCode);

    @Query("SELECT c FROM Courses c WHERE c.courseId in ?1")
    List<Course> findByListId(List<String> courseId);

    Page<Course> findByCourseCodeLikeOrCourseNameLikeOrDescriptionLike(String code,String name,String description,Pageable pageable);
}
