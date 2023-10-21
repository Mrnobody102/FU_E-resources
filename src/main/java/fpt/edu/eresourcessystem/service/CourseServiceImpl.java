package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.Topic;
import fpt.edu.eresourcessystem.repository.CourseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Override
    public List<Course> findAllLecturerCourses(String lecturerId) {
        return courseRepository.findAll();
    }

    @Override
    public Page<Course> findAllLecturerCourses(int pageIndex, int pageSize, String search) {
        return null;
    }

    @Override
    public Page<Course> findAllLecturerCourses(int pageIndex, int pageSize) {
        return null;
    }

    /*
        TOPIC
     */

    @Override
    public boolean addTopic(Topic topic) {
        // check course exist
            Optional<Course> course = courseRepository.findById(topic.getCourseId());
            if(course.isPresent()) {
                Course courseExisted = course.get();

                // get topics of course
                if (null == topic.getTopicId()) {
                    return false;
                }
                List<String> topics = courseExisted.getTopics();
                if (null == courseExisted.getTopics()) {
                    topics = new ArrayList<>();
                }

                // check topic existed in course
                boolean checkTopicExist = false;
                for (int i = 0; i < topics.size(); i++) {
                    if (topics.get(i).equals(topic.getTopicId())) {
                        checkTopicExist = true;
                    }
                }
                // check topic not existed in course
                if (!checkTopicExist) {
                    // add topic to course
                    topics.add(topic.getTopicId());
                    courseExisted.setTopics(topics);
                    courseRepository.save(courseExisted);
                }
            }
                return false;
    }


    @Override
    public Course removeTopic(Topic topic) {
        Course course = findByCourseId(topic.getCourseId());
        if (null != course) {
            // check delete success
            if (null != course.getTopics()) {
                for (int i = 0; i < course.getTopics().size(); i++) {
                    if (course.getTopics().get(i).equals(topic.getTopicId())) {
                        course.getTopics().remove(i);
                        return courseRepository.save(course);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public List<Course> findByListId(List<String> courseIds) {
        List<Course> courses = courseRepository.findByListId(courseIds);
        return courses;
    }

    @Override
    public Page<Course> findByCodeOrNameOrDescription(String code, String name, String description, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<Course> page = courseRepository.findByCourseCodeLikeOrCourseNameLikeOrDescriptionLike( code, name, description,
                pageable);
        return page;
    }

    @Override
    public Page<Course> filterMajor(String major, String code, String name, String description, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<Course> page = courseRepository.filterMajor(major, code, name, description,
                pageable);
        return page;
    }
}
