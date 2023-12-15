package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.enums.CourseEnum;
import fpt.edu.eresourcessystem.enums.DocumentEnum;
import fpt.edu.eresourcessystem.model.*;
import fpt.edu.eresourcessystem.model.elasticsearch.EsCourse;
import fpt.edu.eresourcessystem.repository.CourseRepository;
import fpt.edu.eresourcessystem.repository.elasticsearch.EsCourseRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service("courseService")
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final TopicService topicService;
    private final ResourceTypeService resourceTypeService;

    private final MongoTemplate mongoTemplate;

    private final EsCourseRepository esCourseRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, TopicService topicService, ResourceTypeService resourceTypeService, MongoTemplate mongoTemplate, EsCourseRepository esCourseRepository) {
        this.courseRepository = courseRepository;
        this.topicService = topicService;
        this.resourceTypeService = resourceTypeService;
        this.mongoTemplate = mongoTemplate;
        this.esCourseRepository = esCourseRepository;
    }

    @Override
    public Course addCourse(Course course) {
        if (null != course && null == course.getId()) {
            if (null != courseRepository.findByCourseCode(course.getCourseCode())) {
                return null;
            } else {
                Course result = courseRepository.save(course);
                List<ResourceType> resourceTypes = new ArrayList<>();
                List<String> defaultRt = Arrays.stream(DocumentEnum.DefaultTopicResourceTypes.values())
                        .map(DocumentEnum.DefaultTopicResourceTypes::getDisplayValue)
                        .collect(Collectors.toList());
                for (String rt : defaultRt) {
                    ResourceType resourceType = new ResourceType(rt, result);
                    ResourceType addedResourceType = resourceTypeService.addResourceType(resourceType);
                    resourceTypes.add(addedResourceType);
                }
                result.setResourceTypes(resourceTypes);
                courseRepository.save(result);
                return result;
            }
        }
        return null;
    }

    @Override
    public Course updateCourse(Course course) {
        Optional<Course> savedCourseOpt = courseRepository.findById(course.getId());
        if (savedCourseOpt.isPresent()) {
            Course savedCourse = savedCourseOpt.get();
            savedCourse.setCourseCode(course.getCourseCode());
            savedCourse.setCourseName(course.getCourseName());
            savedCourse.setDescription(course.getDescription());
            savedCourse.setTrainingType(course.getTrainingType());
            savedCourse.setStatus(course.getStatus());
            return courseRepository.save(savedCourse);
        }
        return null;
    }

    @Override
    public Course findByCourseId(String courseId) {
        Course course = courseRepository.findByIdAndDeleteFlg(courseId, CommonEnum.DeleteFlg.PRESERVED);
        return course;
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
        if (check.isPresent()) {
            // Soft delete topic first
            for (Topic topic : course.getTopics()) {
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
        if (check.isPresent()) {
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

    @Override
    public List<Course> findNewCoursesByLecturer(Lecturer lecturer) {
        Criteria criteria = new Criteria();

        // Sort by the "time" in descending order to get the most recent courses
        criteria.and("createdDate").exists(true);
        criteria.and("lecturer.id").is(lecturer.getId());
        criteria.and("status").is("NEW");
        Query query = new Query(criteria).with(Sort.by(Sort.Order.desc("createdDate")));
        // Use a Pageable to limit the result set to 5 documents
        PageRequest pageable = PageRequest.of(0, 5);
        query.with(pageable);
        List<Course> results = mongoTemplate.find(query, Course.class);
        return results;
    }

    /*
        TOPIC
     */

    @Override
    public boolean addTopic(Topic topic) {
        // check course exist
        Optional<Course> course = courseRepository.findById(topic.getCourse().getId());
        if (course.isPresent()) {
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
    public void removeTopic(String courseId, ObjectId topicId) {
        Query query = new Query(Criteria.where("id").is(courseId));
        Update update = new Update().pull("topics", topicId);
        mongoTemplate.updateFirst(query, update, Course.class);
    }

    @Override
    public void removeResourceType(String courseId, ObjectId rsId) {
        Query query = new Query(Criteria.where("id").is(courseId));
        Update update = new Update().pull("resourceTypes", rsId);
        mongoTemplate.updateFirst(query, update, Course.class);
    }

    // Resource type
    @Override
    public boolean addResourceType(ResourceType resourceType) {
        // check course exist
        Optional<Course> course = courseRepository.findById(resourceType.getCourse().getId());
        if (course.isPresent()) {
            Course courseExisted = course.get();

            // get resourceTypes of course
            if (null == resourceType.getId()) {
                return false;
            }
            List<ResourceType> resourceTypes = courseExisted.getResourceTypes();
            if (null == courseExisted.getResourceTypes()) {
                resourceTypes = new ArrayList<>();
            }

            // check topic existed in course
            boolean checkTopicExist = false;
            for (int i = 0; i < resourceTypes.size(); i++) {
                if (resourceTypes.get(i).equals(resourceType.getId())) {
                    checkTopicExist = true;
                }
            }
            // check topic not existed in course
            if (!checkTopicExist) {
                // add topic to course
                resourceTypes.add(resourceType);
                courseExisted.setResourceTypes(resourceTypes);
                courseRepository.save(courseExisted);
            }
        }
        return false;
    }

    @Override
    public void addResourceTypeToCourse(Course course, ObjectId resourceTypeId) {
        Query query = new Query(Criteria.where("id").is(course.getId()));
        Update update = new Update().push("resourceTypes", resourceTypeId);
        mongoTemplate.updateFirst(query, update, Course.class);
    }

    @Override
    public void addStudentSaveToCourse(String courseId, String studentMail) {
        Query query = new Query(Criteria.where("id").is(courseId));
        Update update = new Update().push("students", studentMail);
        mongoTemplate.updateFirst(query, update, Course.class);
    }

    @Override
    public void removeStudentUnsaveFromCourse(String courseId, String studentMail) {
        Query query = new Query(Criteria.where("id").is(courseId));
        Update update = new Update().pull("students", studentMail);
        mongoTemplate.updateFirst(query, update, Course.class);
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
        Page<Course> page = courseRepository.findByCourseCodeLikeOrCourseNameLikeOrDescriptionLike(code, name, description,
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
                Criteria.where("courseCode").regex(Pattern.quote(courseCode.trim()), "i"),
                Criteria.where("courseName").regex(Pattern.quote(courseName.trim()), "i")
        );
        criteria.and("status").is(CourseEnum.Status.PUBLISH);
        criteria.and("deleteFlg").is(CommonEnum.DeleteFlg.PRESERVED);

        Query query = new Query(criteria).with(pageable);
        List<Course> results = mongoTemplate.find(query, Course.class);
        Page<Course> page = PageableExecutionUtils.getPage(results, pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Course.class));
        return page;
    }

    @Override
    public Page<Course> findByCourseNameLike(String courseName, Integer pageIndex, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Criteria criteria = new Criteria();
        criteria.orOperator(
                Criteria.where("courseName").regex(Pattern.quote(courseName.trim()), "i"),
                criteria.where("status").is(CourseEnum.Status.PUBLISH),
                criteria.where("deleteFlg").is(CommonEnum.DeleteFlg.PRESERVED)
        );

        Query query = new Query(criteria).with(pageable);
        List<Course> results = mongoTemplate.find(query, Course.class);
        Page<Course> page = PageableExecutionUtils.getPage(results, pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Course.class));
        return page;
    }

    @Override
    public Page<Course> findByCourseCodeLike(String courseCode, Integer pageIndex, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Criteria criteria = new Criteria();
        criteria.orOperator(
                Criteria.where("courseCode").regex(Pattern.quote(courseCode.trim()), "i"),
                criteria.where("status").is(CourseEnum.Status.PUBLISH),
                criteria.where("deleteFlg").is(CommonEnum.DeleteFlg.PRESERVED)
        );

        Query query = new Query(criteria).with(pageable);
//        long total = mongoTemplate.count(query, Course.class);
//        Page<Course> page = new PageImpl<>(results, pageable, total);
//        return page;
        List<Course> results = mongoTemplate.find(query, Course.class);
        Page<Course> page = PageableExecutionUtils.getPage(results, pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Course.class));
        return page;
    }

    @Override
    public Course updateLectureId(String courseId, Lecturer newLecture) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);

        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            course.setLecturer(newLecture);
            course.setStatus(CourseEnum.Status.NEW);
            return courseRepository.save(course);
        } else {
            return null;
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

    @Override
    public SearchPage<EsCourse> searchCourse(String search, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return esCourseRepository.customSearch(search, pageable);
    }

    @Override
    public long countTotalCourses() {
        return courseRepository.count();
    }

    @Override
    public Page<Course> findByListCourseIdAndSearch(String search, List<String> courseIds,  int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex-1, pageSize);
        Criteria criteria = new Criteria();
        criteria.and("id").in(courseIds);
        if (search != null && !search.isEmpty()) {
            Criteria regexCriteria = new Criteria().orOperator(
                    Criteria.where("courseName").regex(Pattern.quote(search), "i"),
                    Criteria.where("courseCode").regex(Pattern.quote(search), "i")
            );
            criteria.andOperator(regexCriteria);
        }
        Query query = new Query(criteria);
        long total = mongoTemplate.count(query, Course.class);
        System.out.println(total);
        List<Course> courses = mongoTemplate.find(query.with(pageable), Course.class);
        return new PageImpl<>(courses, pageable, total);
        //        return courseRepository.findByCourseNameIgnoreCaseContainingOrCourseCodeIgnoreCaseContainingAndIdIn(
//                search,search,courseIds, pageable);
    }


}
