package fpt.edu.eresourcessystem.repository;


import fpt.edu.eresourcessystem.model.Lecturer;
import fpt.edu.eresourcessystem.model.LecturerCourse;
import fpt.edu.eresourcessystem.model.LecturerCourseId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("lecturerCourseRepository")
public interface LecturerCourseRepository extends MongoRepository<LecturerCourse, LecturerCourseId> {
    Optional<LecturerCourse> findById(LecturerCourseId lecturerCourseId);
    @Query("{ 'lecturerCourseId.courseId' : ?0, 'lecturerCourseId.lastModifiedDate' : null }")
    LecturerCourse findCurrentCourseLecturer(String courseId);

    List<LecturerCourse> findLecturerCoursesById(Lecturer lecturer);
}
