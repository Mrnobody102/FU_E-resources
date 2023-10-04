package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.dto.ObjectRespond;
import fpt.edu.eresourcessystem.exception.CourseNotExistedException;
import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.service.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manager/courses")
public class ManagerCourseController {
    private final CourseService courseService;

    public ManagerCourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/add")
    public ObjectRespond addCourse(Course course){
        courseService.addCourse(course);
        return new ObjectRespond("success", course);
    }

    @PutMapping("/update")
    public ResponseEntity updateCourse(Course course) throws CourseNotExistedException {
        courseService.updateCourse(course);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/list")
    public ResponseEntity<List<Course>> findAll(){
        return ResponseEntity.ok(courseService.findAll());
    }

    @GetMapping("/list/{pageIndex}")
    Page<Course> findCourseByPage(@PathVariable Integer pageIndex, String search){
        return null;
    }

    @DeleteMapping("delete")
    public String delete(String id){
        return null;
    }


}
