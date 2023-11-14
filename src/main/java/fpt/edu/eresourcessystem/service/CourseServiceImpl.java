package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.enums.CourseEnum;
import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.Lecturer;
import fpt.edu.eresourcessystem.model.Librarian;
import fpt.edu.eresourcessystem.model.Topic;
import fpt.edu.eresourcessystem.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("courseService")
public class CourseServiceImpl implements CourseService{
    private final CourseRepository courseRepository;
    private final TopicService topicService;

    private final MongoTemplate mongoTemplate;


    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, TopicService topicService, MongoTemplate mongoTemplate) {
        this.courseRepository = courseRepository;
        this.topicService = topicService;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Course addCourse(Course course) {
        if(null!=course && null==course.getId()){
            if(null!=courseRepository.findByCourseCode(course.getCourseCode())){
                return null;
            }else {
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
    public boolean softDelete(Course course) {
        Optional<Course> check = courseRepository.findById(course.getId());
        if(check.isPresent()){
            // Soft delete topic first
            for(Topic topic:course.getTopics()){
                topicService.softDelete(topic);
            }

            // SOFT DELETE Course
            course.setDeleteFlg(CommonEnum.DeleteFlg.DELETED);
            courseRepository.save(course);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Course course) {
        Optional<Course> check = courseRepository.findById(course.getId());
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
                List<Topic> topics = courseExisted.getTopics();
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
                    topics.add(topic);
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
        Query query = new Query(Criteria.where("id").in(courseIds));
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
    public Page<Course> findByStatus(String status, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<Course> page = courseRepository.findByStatus(status,
                pageable);
        return page;
    }


    @Override
    public Page<Course> findByCourseNameOrCourseCode(String courseName, String courseCode, Integer pageIndex, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Criteria criteria = new Criteria();
        criteria.orOperator(
                Criteria.where("courseCode").regex(courseCode.trim(), "i"),
                Criteria.where("courseName").regex(courseName.trim(), "i")
        );
        criteria.and("status").is(CourseEnum.Status.PUBLISH);
        criteria.and("deleteFlg").is(CommonEnum.DeleteFlg.PRESERVED);

        Query query = new Query(criteria).with(pageable);
        List<Course> results = mongoTemplate.find(query, Course.class);
        Page<Course> page =  PageableExecutionUtils.getPage(results, pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Course.class));
        System.out.println(page.getTotalPages());
        return page;
    }

    @Override
    public Page<Course> findByCourseNameLike(String courseName, Integer pageIndex, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Criteria criteria = new Criteria();
        criteria.orOperator(
                Criteria.where("courseName").regex(courseName.trim(), "i"),
                criteria.where("status").is(CourseEnum.Status.PUBLISH),
                criteria.where("deleteFlg").is(CommonEnum.DeleteFlg.PRESERVED)
        );

        Query query = new Query(criteria).with(pageable);
        List<Course> results = mongoTemplate.find(query, Course.class);
        Page<Course> page =  PageableExecutionUtils.getPage(results, pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Course.class));
        return page;
    }

    @Override
    public Page<Course> findByCourseCodeLike(String courseCode, Integer pageIndex, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Criteria criteria = new Criteria();
        criteria.orOperator(
                Criteria.where("courseCode").regex(courseCode.trim(), "i"),
                criteria.where("status").is(CourseEnum.Status.PUBLISH),
                criteria.where("deleteFlg").is(CommonEnum.DeleteFlg.PRESERVED)
        );

        Query query = new Query(criteria).with(pageable);
//        long total = mongoTemplate.count(query, Course.class);
//        Page<Course> page = new PageImpl<>(results, pageable, total);
//        return page;
        List<Course> results = mongoTemplate.find(query, Course.class);
        Page<Course> page =  PageableExecutionUtils.getPage(results, pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Course.class));
        return page;
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

    public boolean removeLecture(String courseId) {
        Course course = courseRepository.findById(courseId).orElse(null);

        if (course == null) {
            return false;
        }
        course.setLecturer(null); // Assuming lecturer is a reference to the lecture
        courseRepository.save(course);
        return true;
    }



}
