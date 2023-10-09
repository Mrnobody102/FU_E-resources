package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.dto.ObjectRespond;
import fpt.edu.eresourcessystem.exception.CourseNotExistedException;
import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.service.AccountService;
import fpt.edu.eresourcessystem.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/librarian/courses")
public class LibrarianCourseController {
    private final CourseService courseService;
    private final AccountService accountService;

    public LibrarianCourseController(CourseService courseService, AccountService accountService) {
        this.courseService = courseService;
        this.accountService = accountService;
    }

    //Just for test add course screen
    @GetMapping({"/add"})
    public String addProcess(final Model model){
        List<Account> lecturers = accountService.findAllLecturer();
        model.addAttribute("course", new Course());
        model.addAttribute("lecturers", lecturers);
        return "course/add_course";
    }

    @PostMapping("/add")
    public String addCourse(@ModelAttribute Course course, @RequestParam String lesson, @RequestParam String lecturer){
        Course checkExist = courseService.findByCourseId(course.getCourseId());
        if(null==checkExist){
            courseService.addCourse(course);
            return "redirect:/librarian/courses/add?success";
        }else return "redirect:/librarian/courses/add?error";

    }



    @PutMapping("/update")
    public ResponseEntity updateCourse(Course course) throws CourseNotExistedException {
        courseService.updateCourse(course);
        return ResponseEntity.ok().build();
    }

    @GetMapping({"/list"})
    public String showCourse(final Model model){
        List<Course> courses = courseService.findAll();
        model.addAttribute("courses", courses);
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

    @DeleteMapping("delete")
    public String delete(String id){
        return null;
    }


}
