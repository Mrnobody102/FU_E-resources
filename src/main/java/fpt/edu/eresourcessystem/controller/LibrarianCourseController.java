package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.dto.ObjectRespond;
import fpt.edu.eresourcessystem.exception.CourseNotExistedException;
import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/librarian/courses")
public class LibrarianCourseController {
    private final CourseService courseService;

    public LibrarianCourseController(CourseService courseService) {
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

    @GetMapping({"/list"})
    public String showCourse(){
        return "librarian/librarian_courses";
    }

//    @GetMapping({"/list","/list/{pageIndex}"})
//    public String showCourse(@PathVariable String pageIndex){
//
//        return "librarian/librarian_courses";
//    }

    //Just for test detail screen
    @GetMapping({"/list/1"})
    public String showDetailCourse(){

        return "course/detail_course";
    }

    //Just for test add course screen
    @GetMapping({"/add"})
    public String addCourse(){

        return "course/add_course";
    }

    @DeleteMapping("delete")
    public String delete(String id){
        return null;
    }


}
