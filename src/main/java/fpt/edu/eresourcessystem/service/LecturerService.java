package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.Lecturer;
import fpt.edu.eresourcessystem.model.LecturerCourse;
import fpt.edu.eresourcessystem.model.Student;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LecturerService {
    Lecturer findByCourseId(String courseId);
    Lecturer addLecturer(Lecturer lecturer);
    List<Lecturer> findAll();

    List<Course> findListManageCourse(Lecturer lecturer);

    Lecturer findByAccountId(String accountId);

    List<Lecturer> findByListLecturerIds(List<String> ids);

    Lecturer findCurrentCourseLecturer(String courseId);

    Page<Lecturer> findLecturerByLecturerIdLike(String lectureId, int pageIndex, int pageSize);

    Page<Course> findListManagingCourse(Lecturer lecturer, int pageIndex, int pageSize);
}
