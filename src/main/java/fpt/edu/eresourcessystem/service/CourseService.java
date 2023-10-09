package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.exception.CourseNotExistedException;
import fpt.edu.eresourcessystem.exception.CourseNotFoundException;
import fpt.edu.eresourcessystem.model.Course;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CourseService {
    void addCourse(Course Course);
    Course updateCourse(Course Course);
    Course findByCourseId(String username);
    List<Course> findAll();
    Page<Course> findAll(int pageIndex, int pageSize, String search);
    boolean deleteById(String id);
    boolean delete(Course product);
    Page<Course> findAll(int pageIndex, int pageSize);

}
