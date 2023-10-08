package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.exception.CourseNotExistedException;
import fpt.edu.eresourcessystem.exception.CourseNotFoundException;
import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.repository.CourseRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service("courseService")
public class CourseServiceImpl implements CourseService{
    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public void addCourse(Course Course) {
        courseRepository.insert(Course);
    }

    @Override
    public Course updateCourse(Course course){
        Optional<Course> savedCourse = courseRepository.findById(course.getCourseId());
        if(savedCourse.isPresent()){
            courseRepository.save(course);
        }

        return null;
    }

    @Override
    public Course findByCourseId(String courseId){
        Optional<Course> course = courseRepository.findByCourseId(courseId);
        return course.orElse(null);
    }

    @Override
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    public Page<Course> findAll(int pageIndex, int pageSize, String search) {
        return null;
    }

    @Override
    public boolean deleteById(String id) {
        return false;
    }

    @Override
    public boolean delete(Course product) {
        return false;
    }

    @Override
    public Page<Course> findAll(int pageIndex, int pageSize) {
        return null;
    }
}
