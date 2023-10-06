package fpt.edu.eresourcessystem.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("lecturer/courses")
public class LecturerCourseController {
    @GetMapping("/list")
    public String courseList(){
        return "lecturer/lecturer_courses";
    }

}
