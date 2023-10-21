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

    public Student getLoggedInStudent() {
        return studentService.findAll().get(0);
    }


    /*
        HOME
     */

    @GetMapping({"", "/home"})
    public String getStudentHome(@ModelAttribute Account account, final Model model) {
        List<Course> courses = courseService.findAll();
        model.addAttribute("courses", courses);
        model.addAttribute("majors", CourseEnum.Major.values());
        return "student/student_home";
    }

    /*
        COURSE
     */

    @GetMapping("/courses")
    public String getStudentCourse(@ModelAttribute Account account, final Model model) {
        List<Course> courses = courseService.findAll();
        model.addAttribute("courses", courses);
        model.addAttribute("majors", CourseEnum.Major.values());
        return "student/course/student_courses";
    }

    @GetMapping("/courses/{courseId}")
    public String viewCourseDetail(@PathVariable String courseId, final Model model) {
        // auth
        Student student = getLoggedInStudent();

        Course course = courseService.findByCourseId(courseId);
        List<Topic> topics = topicService.findByCourseId(courseId);
        model.addAttribute("course", course);
        model.addAttribute("topics", topics);
        if (studentService.checkCourseSaved(student.getStudentId(), courseId)) {
            model.addAttribute("saved", true);
        } else model.addAttribute("saved", false);
        return "student/course/student_course-detail";
    }

    @GetMapping("/courses/{courseId}/save_course")
    public String saveCourse(@PathVariable String courseId) {
        // get account authorized
        Student student = getLoggedInStudent();
        if (null != courseService.findByCourseId(courseId)) {
            studentService.saveACourse(student.getStudentId(), courseId);
        }
        return "redirect:/student/courses/" + courseId + "?success";
    }

    @GetMapping("/courses/{courseId}/unsaved_course")
    public String unsavedCourse(@PathVariable String courseId) {
        // get account authorized
        Student student = getLoggedInStudent();

        if (null != courseService.findByCourseId(courseId)) {
            studentService.unsavedACourse(student.getStudentId(), courseId);
        }
        return "redirect:/student/courses/" + courseId + "?success";
    }

    /*
        STUDENT PERSONAL LIBRARY
     */

    @GetMapping({"/my_library/saved_courses", "/my_library"})
    public String viewCourseSaved(final Model model) {
        // get account authorized
        Student student = getLoggedInStudent();
        List<String> savedCourses = student.getSavedCourses();
        List<Course> courses = new ArrayList<>();
        if (null != savedCourses) {
            for (String cId : savedCourses) {
                Course course = courseService.findByCourseId(cId);
                courses.add(course);
            }
        }
        model.addAttribute("coursesSaved", courses);
        return "student/library/student_saved_courses";

    }

}
