package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.service.AccountService;
import fpt.edu.eresourcessystem.service.CourseService;
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
        return "librarian/course/librarian_add_course";
    }

    @PostMapping("/add")
    public String addCourse(@ModelAttribute Course course, @RequestParam String lesson, @RequestParam String lecturer){

        Course checkExist = courseService.findByCourseCode(course.getCourseCode());
        if(null==checkExist){
            courseService.addCourse(course);
            return "redirect:/librarian/courses/add?success";
        }else return "redirect:/librarian/courses/add?error";

    }

    @GetMapping({"/update/{courseId}","/update"})
    public String updateProcess(@PathVariable(required = false) String courseId,final Model model)  {
        if(null==courseId){
            courseId="";
        }
        Course course = courseService.findByCourseId(courseId);
//        System.out.println(account);
        if(null==course){
            return "redirect:librarian/courses/update?error";
        }else{
            List<Account> lecturers = accountService.findAllLecturer();
            model.addAttribute("lecturers", lecturers);
            model.addAttribute("course", course);
            return "librarian/course/librarian_update_course";
        }
    }

    @PostMapping("/update")
    public String updateCourse(@ModelAttribute Course course, final  Model model) {
        Course checkExist = courseService.findByCourseId(course.getCourseId());
        if(null==checkExist){
            model.addAttribute("errorMessage","course not exist.");
            return "exception/404";
        }else{
            Course checkCodeDuplicate = courseService.findByCourseCode(course.getCourseCode());
            if( checkCodeDuplicate != null &&
                    !checkExist.getCourseCode().toLowerCase().equals(course.getCourseCode())){
//                return "redirect:/librarian/courses/update?error";

            }
            courseService.updateCourse(course);
//            System.out.println(result);
            model.addAttribute("course", course);
            List<Account> lecturers = accountService.findAllLecturer();
            model.addAttribute("lecturers", lecturers);
            model.addAttribute("success","");
            return "librarian/course/librarian_update_course";
        }
    }

    @GetMapping({"/list"})
    public String showCourse(final Model model){
        List<Course> courses = courseService.findAll();
        model.addAttribute("courses", courses);
        return "librarian/course/librarian_courses";
    }

    @GetMapping({"/detail/{courseId}"})
    public String showCourseDetail(@PathVariable String courseId, final Model model){
        Course course = courseService.findByCourseId(courseId);
        model.addAttribute("course" , course);
        return "librarian/course/librarian_detail_course";
    }


    @DeleteMapping("/delete/{courseId}")
    public String delete(@PathVariable String courseId){
        Course checkExist = courseService.findByCourseId(courseId);
//        System.out.println(checkExist);
        if (null != checkExist){
            courseService.delete(checkExist);
            return "redirect:/librarian/courses/list?success";
        }
        return "redirect:/librarian/courses/list?error";
    }


}
