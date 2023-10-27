package fpt.edu.eresourcessystem.controller.restcontrollers;

import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.Topic;
import fpt.edu.eresourcessystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseRestController {
    private final AccountService accountService;
    private final LibrarianService librarianService;
    private final LecturerService lecturerService;
    private final StudentService studentService;
    private final CourseService courseService;
    private final TopicService topicService;
    private final DocumentService documentService;

    private final LecturerCourseService lecturerCourseService;

    @Autowired
    public CourseRestController(AccountService accountService, LibrarianService librarianService, LecturerService lecturerService, StudentService studentService, CourseService courseService, TopicService topicService, DocumentService documentService, LecturerCourseService lecturerCourseService) {
        this.accountService = accountService;
        this.librarianService = librarianService;
        this.lecturerService = lecturerService;
        this.studentService = studentService;
        this.courseService = courseService;
        this.topicService = topicService;
        this.documentService = documentService;
        this.lecturerCourseService = lecturerCourseService;
    }


    /**
     * API
     * Get courses by course ID
     * @param keyword
     * @return list of courses
     */
    @GetMapping()
    public ResponseEntity<List<Course>> searchCourses(@RequestParam("keyword") String keyword) {
        List<Course> courses = courseService.findByCodeOrName(keyword, keyword);
        return ResponseEntity.ok(courses);
    }

    /**
     * API
     * Get topics by course ID
     * @param courseId
     * @return list topics of this course
     */
    @GetMapping("/{courseId}/topics")
    public ResponseEntity<List<Topic>> getTopicsByCourse(@PathVariable String courseId) {
        List<Topic> topics = topicService.findByCourseId(courseId);
        return ResponseEntity.ok(topics);
    }
}
