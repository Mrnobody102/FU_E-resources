package fpt.edu.eresourcessystem.repository;

import fpt.edu.eresourcessystem.model.Account;
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

    @Query("{$and: [{major: ?0}, {$or: [{code: {$regex: ?1}}, {name: {$regex: ?2}}, {description: {$regex: ?3}}]}]}")
    Page<Course> filterMajor(String major, String code, String name, String description, Pageable pageable);

    Page<Course> findByCourseNameLikeOrCourseCodeLike(String courseName, String courseCode, Pageable pageable);
    Page<Course> findByCourseName(String courseName, Pageable pageable);
    Page<Course> findByCourseCode(String courseCode, Pageable pageable);

}
