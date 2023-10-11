package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.Lecturer;
import fpt.edu.eresourcessystem.model.Topic;
import fpt.edu.eresourcessystem.service.AccountService;
import fpt.edu.eresourcessystem.service.CourseService;
import fpt.edu.eresourcessystem.service.LecturerService;
import fpt.edu.eresourcessystem.service.TopicService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/librarian/courses")
public class CourseController {
    private final CourseService courseService;
    private final AccountService accountService;

    private final LecturerService lecturerService;

    private final TopicService topicService;

    public CourseController(CourseService courseService, AccountService accountService, LecturerService lecturerService, TopicService topicService) {
        this.courseService = courseService;
        this.accountService = accountService;
        this.lecturerService = lecturerService;
        this.topicService = topicService;
    }

    //Just for test add course screen
    @GetMapping({"/add"})
    public String addProcess(final Model model){
        List<Account> lecturers = accountService.findAllLecturer();
        model.addAttribute("course", new Course());
        model.addAttribute("lecturers", lecturers);
        return "librarian/course/librarian_add-course";
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
            return "librarian/course/librarian_update-course";
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
            return "librarian/course/librarian_update-course";
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
        List<Topic> topics = topicService.findByCourseId(courseId);
        model.addAttribute("course" , course);
        model.addAttribute("topics" , topics);
        return "librarian/course/librarian_course-detail";
    }


    @GetMapping ("/delete/{courseId}")
    public String delete(@PathVariable String courseId){
        Course checkExist = courseService.findByCourseId(courseId);
//        System.out.println(checkExist);
        if (null != checkExist){
            for (String topicId: checkExist.getTopics()) {
                topicService.delete(topicId);
            }
            courseService.delete(checkExist);
            return "redirect:/librarian/courses/list?success";
        }
        return "redirect:/librarian/courses/list?error";
    }

    @GetMapping({"/updateLecturers/{courseId}"})
    public  String updateLecturersProcess(@PathVariable String courseId,@RequestParam String search, final Model model){
        List<Lecturer> courseLecturers = lecturerService.findByCourseId(courseId);
        List<Account> lecturers = accountService.searchLecturer(search);
        model.addAttribute("courseLecturers", courseLecturers);
        model.addAttribute("lecturers", lecturers);
        return "librarian/course/librarian_add-lecturer-to-course";
    }

    @PostMapping({"/addLecturers/{courseId}"})
    public  String addLecturersProcess(@PathVariable String courseId,@RequestParam String accountId, final Model model){
        List<Lecturer> courseLecturers = lecturerService.findByCourseId(courseId);
        List<Account> lecturers = accountService.searchLecturer("");
        model.addAttribute("courseLecturers", courseLecturers);
        model.addAttribute("lecturers", lecturers);
        return "librarian/course/librarian_add-lecturer-to-course";
    }

    @GetMapping({"/addTopic/{courseId}", "/topics/{courseId}"})
    public String addTopicProcess(@PathVariable String courseId, final Model model){
        Course course = courseService.findByCourseId(courseId);
        List<Topic> topics = topicService.findByCourseId(courseId);
//        System.out.println(topics);
        model.addAttribute("course", course);
        model.addAttribute("topics", topics);
        Topic topic = new Topic();
        topic.setCourseId(courseId);
        model.addAttribute("topic",topic);
        return "librarian/course/librarian_add-topic-to-course";
    }

    @PostMapping({"/addTopic"})
    public String addTopic(@ModelAttribute Topic topic, final Model model){
        System.out.println(topic);
        topic = topicService.addTopic(topic);
        courseService.addTopic(topic);
        Course course = courseService.findByCourseId(topic.getCourseId());
        List<Topic> topics = topicService.findByCourseId(topic.getCourseId());
        model.addAttribute("course", course);
        model.addAttribute("topics", topics);
        Topic modelTopic = new Topic();
        topic.setCourseId(course.getCourseId());
        model.addAttribute("topic",modelTopic);
        return "librarian/course/librarian_add-topic-to-course";
    }

    @GetMapping({"/updateTopic/{topicId}"})
    public String editTopicProcess(@PathVariable String topicId, final Model model){
        Topic topic = topicService.findById(topicId);
        Course course = courseService.findByCourseId(topic.getCourseId());
        List<Topic> topics = topicService.findByCourseId(course.getCourseId());
        model.addAttribute("course", course);
        model.addAttribute("topics", topics);
        model.addAttribute("topic",topic);
        return "librarian/course/librarian_update-topic-of-course";
    }


    @PostMapping({"/updateTopic/{topicId}"})
    public String editTopic(@PathVariable String topicId ,@ModelAttribute Topic topic, final Model model){
        Topic checkTopicExist = topicService.findById(topicId);
        if(null!=checkTopicExist){
            topicService.updateTopic(topic);
            return "redirect:/librarian/courses/updateTopic/"+ topicId;
        }return "librarian/course/librarian_add-topic-to-course";

    }

    @GetMapping({"/deleteTopic/{courseId}/{topicId}"})
    public String deleteTopic( @PathVariable String courseId, @PathVariable String topicId, final Model model){
        Topic topic = topicService.findById(topicId);
        if(null != topic){
            courseService.removeTopic(topic);
            topicService.delete(topicId);
            Course course = courseService.findByCourseId(courseId);
            List<Topic> topics = topicService.findByCourseId(courseId);
            Topic modelTopic = new Topic();
            topic.setCourseId(courseId);
            model.addAttribute("course", course);
            model.addAttribute("topics", topics);
            model.addAttribute("topic", modelTopic);
            return "librarian/course/librarian_add-topic-to-course";
        }
        return "librarian/course/librarian_add-topic-to-course";
    }
}
