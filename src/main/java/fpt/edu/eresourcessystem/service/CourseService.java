package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.*;
import fpt.edu.eresourcessystem.model.elasticsearch.EsCourse;
import fpt.edu.eresourcessystem.model.elasticsearch.EsDocument;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.SearchPage;

import java.util.List;

public interface CourseService {
    Course addCourse(Course Course);
    Course updateCourse(Course Course);

    boolean softDelete(Course course);

    boolean delete(Course course);

    /*
        LIBRARIAN
     */
    Course findByCourseId(String courseId);
    List<Course> findAll();
    List<Course> findCourseByLibrarian(String email);
    Page<Course> findAll(int pageIndex, int pageSize, String search);

    Course findByCourseCode(String courseCode);

    Page<Course> findAll(int pageIndex, int pageSize);

    /*
        LECTURER
     */
    List<Course> findAllLecturerCourses(String lecturerId);
    Page<Course> findAllLecturerCourses(int pageIndex, int pageSize, String search);

    Page<Course> findAllLecturerCourses(int pageIndex, int pageSize);

    List<Course> findCoursesByLibrarian(Librarian librarian);

    List<Course> findNewCoursesByLecturer(Lecturer lecturer);

    boolean addTopic(Topic topic);

    void removeTopic(String courseId, ObjectId topicId);

    void removeResourceType(String courseId, ObjectId rsId);

    // Resource type
    boolean addResourceType(ResourceType resourceType);

    List<Course> findByListId(List<String> courseIds);

    Page<Course> findByCodeOrNameOrDescription(String code, String name, String description, int pageIndex, int pageSize);
    List<Course> findByCodeOrName(String code, String name);

    Page<Course> findByStatus(String status, int pageIndex, int pageSize);

    Page<Course> findByCourseNameOrCourseCode(String courseName, String courseCode, Integer pageIndex, Integer pageSize);

    Page<Course> findByCourseNameLike(String courseName, Integer pageIndex, Integer pageSize);
    Page<Course> findByCourseCodeLike(String courseCode, Integer pageIndex, Integer pageSize);

    Course updateLectureId(String courseId, Lecturer newLecture);

    boolean removeLecture(String courseId);
    SearchPage<EsCourse> searchCourse(String search, int pageIndex, int pageSize);
}
