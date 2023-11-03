package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.model.*;
import fpt.edu.eresourcessystem.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("courseService")
public class CourseServiceImpl implements CourseService{
    private final CourseRepository courseRepository;

    private final MongoTemplate mongoTemplate;


    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, MongoTemplate mongoTemplate) {
        this.courseRepository = courseRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Course addCourse(Course course) {
        if(null!=course && null==course.getId()){
            if(null!=courseRepository.findByCourseCode(course.getCourseCode())){
                return null;
            }else {
                course.setDeleteFlg(CommonEnum.DeleteFlg.PRESERVED);
                Course result = courseRepository.save(course);
                return result;
            }
        }
        return null;

    }

    @Override
    public Course updateCourse(Course course){
        Optional<Course> savedCourse = courseRepository.findById(course.getId());
        if(savedCourse.isPresent()){
           Course result =  courseRepository.save(course);
           return result;
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
        Optional<Course> check = courseRepository.findById(course.getId());
        if(check.isPresent()){
            // SOFT DELETE
            course.setDeleteFlg(CommonEnum.DeleteFlg.DELETED);
            courseRepository.save(course);
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
    public List<Course> findByCodeOrName(String code, String name) {
        List<Course> courses = courseRepository.findByCodeOrName(code, name);
        System.out.println(courses);
        return courses;
    }

    @Override
    public Page<Course> findAll(int pageIndex, int pageSize) {
        return null;
    }

    @Override
    public List<Course> findAllLecturerCourses(String lecturerId) {
        return courseRepository.findAll();
    }

    public List<Course> findCoursesByLibrarian(Librarian librarian) {
        List<String> courseIds = librarian.getCreatedCourses()
                .stream()
                .map(Course::getId)
                .collect(Collectors.toList());

        return courseRepository.findCoursesByLibrarianCreatedCourses(courseIds);
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
            Optional<Course> course = courseRepository.findById(topic.getCourse().getId());
            if(course.isPresent()) {
                Course courseExisted = course.get();

                // get topics of course
                if (null == topic.getId()) {
                    return false;
                }
                List<String> topics = courseExisted.getTopics();
                if (null == courseExisted.getTopics()) {
                    topics = new ArrayList<>();
                }

                // check topic existed in course
                boolean checkTopicExist = false;
                for (int i = 0; i < topics.size(); i++) {
                    if (topics.get(i).equals(topic.getId())) {
                        checkTopicExist = true;
                    }
                }
                // check topic not existed in course
                if (!checkTopicExist) {
                    // add topic to course
                    topics.add(topic.getId());
                    courseExisted.setTopics(topics);
                    courseRepository.save(courseExisted);
                }
            }
                return false;
    }


    @Override
    public Course removeTopic(Topic topic) {
        Course course = findByCourseId(topic.getCourse().getId());
        if (null != course) {
            // check delete success
            if (null != course.getTopics()) {
                for (int i = 0; i < course.getTopics().size(); i++) {
                    if (course.getTopics().get(i).equals(topic.getId())) {
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
        Query query = new Query(Criteria.where("courseId").in(courseIds));
        List<Course> courses = mongoTemplate.find(query, Course.class);
        return courses;
    }

    @Override
    public List<Course> findCourseByLibrarian(String email) {
        return courseRepository.findCourseByLibrarianEmail(email);
    }
    @Override
    public Page<Course> findByCodeOrNameOrDescription(String code, String name, String description, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<Course> page = courseRepository.findByCourseCodeLikeOrCourseNameLikeOrDescriptionLike( code, name, description,
                pageable);
        return page;
    }

    @Override
    public Page<Course> findByCourseNameOrCourseCode(String courseName, String courseCode, Integer pageIndex, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        return courseRepository.findByCourseNameLikeOrCourseCodeLike(courseName, courseCode,
                pageable);
    }

    @Override
    public Page<Course> findByCourseNameLike(String courseName, Integer pageIndex, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        return courseRepository.findByCourseNameLike(courseName,
                pageable);
    }

    @Override
    public Page<Course> findByCourseCodeLike(String courseCode, Integer pageIndex, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        return courseRepository.findByCourseCodeLike(courseCode,
                pageable);
    }

    @Override
    public Course updateLectureId(String courseId, Lecturer newLecture) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);

        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            course.setLecturer(newLecture);
            return courseRepository.save(course);
        } else {
            // Xử lý trường hợp không tìm thấy khóa học với courseId
            return null; // hoặc throw một exception tùy ý
        }
    }



}
