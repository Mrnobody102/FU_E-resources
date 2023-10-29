package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.enums.AccountEnum;
import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.Student;
import fpt.edu.eresourcessystem.model.Topic;
import fpt.edu.eresourcessystem.service.CourseService;
import fpt.edu.eresourcessystem.service.StudentService;
import fpt.edu.eresourcessystem.service.TopicService;
import fpt.edu.eresourcessystem.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/student")
@PropertySource("web-setting.properties")
public class StudentController {
    @Value("${page-size}")
    private Integer pageSize;
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
        return "student/student_home";
    }

    /*
        COURSE
     */

    /**
     * Display 5 recent course
     * @param account
     * @param model
     * @return recent courses
     */
    @GetMapping("/courses")
    public String getStudentCourse(@ModelAttribute Account account, final Model model) {
//        List<Course> courses = courseService.findAll();
//        model.addAttribute("courses", courses);
        return "student/course/student_courses";
    }

    @GetMapping({"/courses/{courseId}","/courseDetail"})
    public String viewCourseDetail(@PathVariable(required = false) String courseId, final Model model) {
        // auth
        Student student = getLoggedInStudent();

        Course course = courseService.findByCourseId(courseId);
        List<Topic> topics = topicService.findByCourseId(courseId);
        model.addAttribute("course", course);
        model.addAttribute("topics", topics);
        if (studentService.checkCourseSaved(student.getId(), courseId)) {
            model.addAttribute("saved", true);
        } else model.addAttribute("saved", false);
        return "student/course/student_course-detail";
//        return "student/course/courseDetailFrontEnd";
    }

    @GetMapping("/courses/{courseId}/save_course")
    public String saveCourse(@PathVariable String courseId) {
        // get account authorized
        Student student = getLoggedInStudent();
        if (null != courseService.findByCourseId(courseId)) {
            studentService.saveACourse(student.getId(), courseId);
        }
        return "redirect:/student/courses/" + courseId + "?success";
    }

    @GetMapping("/courses/{courseId}/unsaved_course")
    public String unsavedCourse(@PathVariable String courseId) {
        // get account authorized
        Student student = getLoggedInStudent();

        if (null != courseService.findByCourseId(courseId)) {
            studentService.unsavedACourse(student.getId(), courseId);
        }
        return "redirect:/student/courses/" + courseId + "?success";
    }

    /*
        STUDENT - MY LIBRARY
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


    @GetMapping({"/topic/{topicId}","/topicDetail"})
    public String viewTopicDetail(@PathVariable(required = false) String docId, final Model model) {
        return "student/course/student_view-topic-detail";
    }
    @GetMapping({"/document/{docId}","/documentDetail"})
    public String viewDocumentDetail(@PathVariable(required = false) String docId, final Model model) {
//        // auth
//        Student student = getLoggedInStudent();
        return "student/course/student_document-detail";
    }
    @GetMapping({"/search_course/{pageIndex}"})
    public String viewSearchCourse(@PathVariable Integer pageIndex,
                                     @RequestParam(required = false, defaultValue = "") String search,
                                     @RequestParam(required = false, defaultValue = "all") String filter,
                                     final Model model) {
//        // auth
//        Student student = getLoggedInStudent();
        Page<Course> page;
        if (null == filter || "all".equals(filter)) {
            page = courseService.findByCourseNameOrCourseCode(search, search, pageIndex, pageSize);
        } else if("name".equals(filter)){
            page = courseService.findByCourseNameLike(search, pageIndex, pageSize);
        } else{
            page = courseService.findByCourseCodeLike(search, pageIndex, pageSize);
        }
        List<Integer> pages = CommonUtils.pagingFormat(page.getTotalPages(), pageIndex);
//        System.out.println(pages);
        model.addAttribute("pages", pages);
        model.addAttribute("totalPage", page.getTotalPages());
        System.out.println(page.getTotalPages());
        model.addAttribute("courses", page.getContent());
        model.addAttribute("search", search);
        model.addAttribute("roles", AccountEnum.Role.values());
        model.addAttribute("filter", filter);
        model.addAttribute("currentPage", pageIndex);
        model.addAttribute("search", search);
        return "student/course/student_courses";
    }
}
