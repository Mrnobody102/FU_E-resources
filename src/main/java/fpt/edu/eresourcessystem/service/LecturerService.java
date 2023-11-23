package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.Lecturer;
import org.bson.types.ObjectId;
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

    List<Lecturer> findAllLecture();

    Page<Lecturer> findLecturerByLecturerIdLike(String lectureId, int pageIndex, int pageSize);


    void addCourseToLecturer(String lecturerId, ObjectId courseId);


    boolean removeCourse(String lecturerId, Course courseId);

    Page<Course> findListManagingCourse(Lecturer lecturer, String status, int pageIndex, int pageSize);


    Lecturer findLecturerByAccount(Account account);


    Lecturer findLecturerById(String lectureId);

    boolean update(Lecturer lecturer);

}
