package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.exception.CourseNotExistedException;
import fpt.edu.eresourcessystem.exception.CourseNotFoundException;
import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {
    void addCourse(Course Course);
    Course updateCourse(Course Course);
    Course findByCourseId(String courseId);
    List<Course> findAll();
    Page<Course> findAll(int pageIndex, int pageSize, String search);
    boolean delete(Course course);

    Course findByCourseCode(String courseCode);

    Page<Course> findAll(int pageIndex, int pageSize);

    boolean addTopic(Topic topic);

    Course removeTopic(Topic topic);

    List<Course> findByListId(List<String> courseIds);

    Page<Course> findByCourseCodeLikeOrCourseNameLikeOrDescriptionLike(String code, String name, String description, int pageIndex, int pageSize);

}
