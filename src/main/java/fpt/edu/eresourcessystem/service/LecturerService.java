package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.Lecturer;
import fpt.edu.eresourcessystem.model.LecturerCourse;
import fpt.edu.eresourcessystem.model.Student;

import java.util.List;

public interface LecturerService {
    List<Lecturer> findByCourseId(String courseId);
    Lecturer addLecturer(Lecturer lecturer);

    List<Course> findListManageCourse(Lecturer lecturer);

    Lecturer findByAccountId(String accountId);

    List<Lecturer> findByListLecturerIds(List<String> ids);

    Lecturer findCurrentCourseLecturer(String courseId);

    List<Lecturer> findAll();
}
