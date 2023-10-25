package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.Topic;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CourseService {
    Course addCourse(Course Course);
    Course updateCourse(Course Course);
    boolean delete(Course course);

    /*
        LIBRARIAN
     */
    Course findByCourseId(String courseId);
    List<Course> findAll();
    Page<Course> findAll(int pageIndex, int pageSize, String search);

    Course findByCourseCode(String courseCode);

    Page<Course> findAll(int pageIndex, int pageSize);

    /*
        LECTURER
     */
    List<Course> findAllLecturerCourses(String lecturerId);
    Page<Course> findAllLecturerCourses(int pageIndex, int pageSize, String search);

    Page<Course> findAllLecturerCourses(int pageIndex, int pageSize);

    boolean addTopic(Topic topic);

    Course removeTopic(Topic topic);

    List<Course> findByListId(List<String> courseIds);

    Page<Course> findByCodeOrNameOrDescription(String code, String name, String description, int pageIndex, int pageSize);



    Page<Course> filterMajor(String major, String code, String name, String description, int pageIndex, int pageSize);

    Page<Course> findByCourseNameOrCourseCode(String courseName, String courseCode, Integer pageIndex, Integer pageSize);

    Page<Course> findByCourseNameLike(String courseName, Integer pageIndex, Integer pageSize);
    Page<Course> findByCourseCodeLike(String courseCode, Integer pageIndex, Integer pageSize);
}
