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
    Lecturer updateLecturer(Lecturer lecturer);
    List<Lecturer> findAll();
    Lecturer findByAccountId(String accountId);

    List<Lecturer> findByListLecturerIds(List<String> ids);

    Lecturer findCurrentCourseLecturer(String courseId);

    Page<Lecturer> findLecturerByLecturerIdLike(String lectureId, int pageIndex, int pageSize);

//    Page<Lecturer> findLecturerByCreatedByLikeAndAccount_Name(String createdBy, String search, int pageIndex, int pageSize);

    public Lecturer addLectureWithCourse(Lecturer lecturer);

}
