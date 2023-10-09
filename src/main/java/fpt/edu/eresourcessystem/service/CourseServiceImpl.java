package fpt.edu.eresourcessystem.service;

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
    public void addCourse(Course course) {
        courseRepository.insert(course);
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
        Optional<Course> course = courseRepository.findById(courseId);
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
    public boolean delete(Course course) {
        Optional<Course> check = courseRepository.findById(course.getCourseId());
        if(check.isPresent()){
            courseRepository.delete(course);
            return true;
        }
        return false;
    }

    @Override
    public Course findByCourseCode(String courseCode) {
        Course course = courseRepository.findByCourseCode(courseCode);
        return course;
    }

    @Override
    public Page<Course> findAll(int pageIndex, int pageSize) {
        return null;
    }
}
