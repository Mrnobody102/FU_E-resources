package fpt.edu.eresourcessystem.controller.restcontrollers;

import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.Topic;
import fpt.edu.eresourcessystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseRestController {
    @Value("${page-size}")
    private Integer pageSize;
    private final AccountService accountService;
    private final LibrarianService librarianService;
    private final LecturerService lecturerService;
    private final StudentService studentService;
    private final CourseService courseService;
    private final TopicService topicService;
    private final DocumentService documentService;

    private final LecturerCourseService lecturerCourseService;

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
     *
     * @param keyword
     * @return list of courses
     */
    @GetMapping("/search")
    public ResponseEntity<List<Course>> searchCourses(@RequestParam("keyword") String keyword) {
        List<Course> courses = courseService.findByCodeOrName(keyword, keyword);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{pageIndex}")
    public ResponseEntity<Page<Course>> getCourseByStatus(@RequestParam("status") String status,
                                                          @PathVariable Integer pageIndex) {
        Page<Course> courses = courseService.findByStatus(status, pageIndex, pageSize);
        return ResponseEntity.ok(courses);
    }


}
