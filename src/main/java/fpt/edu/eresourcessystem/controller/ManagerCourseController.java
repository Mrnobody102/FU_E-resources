package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.dto.ObjectRespond;
import fpt.edu.eresourcessystem.exception.CourseNotExistedException;
import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.service.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
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
    @GetMapping({"/list","/list/{pageIndex}"})
    public String findAll(){
        return "lib_manager/lib-manager_subjects";
    }

    @DeleteMapping("delete")
    public String delete(String id){
        return null;
    }


}
