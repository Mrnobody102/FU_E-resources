package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.enums.CourseEnum;
import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.Student;
import fpt.edu.eresourcessystem.model.Topic;
import fpt.edu.eresourcessystem.service.CourseService;
import fpt.edu.eresourcessystem.service.StudentService;
import fpt.edu.eresourcessystem.service.TopicService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final CourseService courseService;

    private final StudentService studentService;

    private final TopicService topicService;

    public StudentController(CourseService courseService, StudentService studentService, TopicService topicService) {
        this.courseService = courseService;
        this.studentService = studentService;
        this.topicService = topicService;
    }

    @GetMapping
    public String getStudentHome(@ModelAttribute Account account, final Model model) {
        List<Course> courses = courseService.findAll();
        model.addAttribute("courses", courses);
        model.addAttribute("majors", CourseEnum.Major.values());
        return "student/student_home";
    }

    @GetMapping("/courses")
    public String getStudentCourse(@ModelAttribute Account account, final Model model) {
        List<Course> courses = courseService.findAll();
        model.addAttribute("courses", courses);
        model.addAttribute("majors", CourseEnum.Major.values());
        return "student/course/student_courses";
    }

    @GetMapping("/courses/{courseId}")
    public String viewCourseDetail(@PathVariable String courseId, final Model model) {
        Account account = new Account();
        account.setAccountId("123");
        Student student = studentService.findByAccountId(account.getAccountId());
        Course course = courseService.findByCourseId(courseId);
        List<Topic> topics = topicService.findByCourseId(courseId);
        model.addAttribute("course", course);
        model.addAttribute("topics", topics);
        if (studentService.checkCourseSaved(student.getAccountId(), courseId)) {
            model.addAttribute("saved", true);
        } else model.addAttribute("saved", false);
        return "student/course/student_course-detail";
    }

    @GetMapping("/courses/save_course/{courseId}")
    public String saveCourse(@PathVariable String courseId, final Model model) {
        // get account authorized
        Account account = new Account();
        account.setAccountId("123");
        Student student = studentService.findByAccountId(account.getAccountId());
        if (null != courseService.findByCourseId(courseId)) {
            studentService.saveACourse(student.getAccountId(), courseId);
        }
        return "redirect:/student/courses/" + courseId + "?success";
    }

    @GetMapping("/courses/unsaved_course/{courseId}")
    public String unsavedCourse(@PathVariable String courseId, final Model model) {
        // get account authorized
        Account account = new Account();
        account.setAccountId("652822485ab106356098b911");
        Student student = studentService.findByAccountId(account.getAccountId());
        if (null != courseService.findByCourseId(courseId)) {
            studentService.unsavedACourse(student.getAccountId(), courseId);
        }
        return "redirect:/student/courses/" + courseId + "?success";
    }

    @GetMapping("/course/saveCourse/list")
    public String viewCourseSaved(final Model model) {
        // get account authorized
        Account account = new Account();
        account.setAccountId("652822485ab106356098b911");
        Student student = studentService.findByAccountId(account.getAccountId());
        List<String> savedCourses = student.getSavedCourses();
        List<Course> courses = new ArrayList<>();
        if (null != savedCourses) {
            for (String cId : savedCourses) {
                Course course = courseService.findByCourseId(cId);
                courses.add(course);
            }
        }
        model.addAttribute("coursesSaved", courses);
        return "student/course/student_courses-saved";

    }

}
