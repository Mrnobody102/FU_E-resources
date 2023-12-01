package fpt.edu.eresourcessystem.controller.restcontrollers;

import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.service.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static fpt.edu.eresourcessystem.constants.Constants.PAGE_SIZE;

@RestController
@RequestMapping("/api/courses")
public class CourseRestController {
    private final CourseService courseService;

    public CourseRestController(CourseService courseService) {
        this.courseService = courseService;
    }


    /**
     * API
     * Get courses by course ID
     *
     * @param keyword search keyword
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
        Page<Course> courses = courseService.findByStatus(status, pageIndex, PAGE_SIZE);
        return ResponseEntity.ok(courses);
    }


}
