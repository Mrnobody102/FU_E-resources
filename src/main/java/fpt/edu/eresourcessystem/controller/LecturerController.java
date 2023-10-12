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

import java.util.List;

@Controller
@RequestMapping
public class LecturerController {
    @GetMapping("/lecturer")
    public String getLibraryManageDashboard(@ModelAttribute Account account) {
        return "lecturer/lecturer";
    }
    private final CourseService courseService;
    private final AccountService accountService;

    private final LecturerService lecturerService;

    private final TopicService topicService;

    public LecturerController(CourseService courseService, AccountService accountService, LecturerService lecturerService, TopicService topicService) {
        this.courseService = courseService;
        this.accountService = accountService;
        this.lecturerService = lecturerService;
        this.topicService = topicService;
    }

    @GetMapping({"/lecturer/courses/update/{courseId}","/lecturer/courses/update"})
    public String updateProcess(@PathVariable(required = false) String courseId,final Model model)  {
        if(null==courseId){
            courseId="";
        }
        Course course = courseService.findByCourseId(courseId);
//        System.out.println(account);
        if(null==course){
            return "redirect:lecturer/courses/update?error";
        }else{
            List<Account> lecturers = accountService.findAllLecturer();
            model.addAttribute("lecturers", lecturers);
            model.addAttribute("course", course);
            return "lecturer/course/lecturer_update-course";
        }
    }

    @PostMapping("/lecturer/courses/update")
    public String updateCourse(@ModelAttribute Course course, final  Model model) {
        Course checkExist = courseService.findByCourseId(course.getCourseId());
        if(null==checkExist){
            model.addAttribute("errorMessage","course not exist.");
            return "exception/404";
        }else{
            Course checkCodeDuplicate = courseService.findByCourseCode(course.getCourseCode());
            if( checkCodeDuplicate != null &&
                    !checkExist.getCourseCode().toLowerCase().equals(course.getCourseCode())){
            }
            courseService.updateCourse(course);
            List<Account> lecturers = accountService.findAllLecturer();
            model.addAttribute("course", course);
            model.addAttribute("lecturers", lecturers);
            model.addAttribute("success","");
            return "lecturer/course/lecturer_update-course";
        }
    }

    @GetMapping({"/lecturer/courses/list"})
    public String showCourse(final Model model){
        List<Course> courses = courseService.findAll();
        model.addAttribute("courses", courses);
        return "lecturer/course/lecturer_courses";
    }

    @GetMapping({"/lecturer/courses/detail/{courseId}"})
    public String showCourseDetail(@PathVariable String courseId, final Model model){
        Course course = courseService.findByCourseId(courseId);
        List<Topic> topics = topicService.findByCourseId(courseId);
        model.addAttribute("course" , course);
        model.addAttribute("topics" , topics);
        return "lecturer/course/lecturer_course-detail";
    }


    @GetMapping ("/lecturer/courses/delete/{courseId}")
    public String delete(@PathVariable String courseId){
        Course checkExist = courseService.findByCourseId(courseId);
        if (null != checkExist){
            for (String topicId: checkExist.getTopics()) {
                topicService.delete(topicId);
            }
            courseService.delete(checkExist);
            return "redirect:/lecturer/courses/list?success";
        }
        return "redirect:/lecturer/courses/list?error";
    }

    @GetMapping({"/lecturer/courses/addTopic/{courseId}", "/lecturer/courses/topics/{courseId}"})
    public String addTopicProcess(@PathVariable String courseId, final Model model){
        Course course = courseService.findByCourseId(courseId);
        List<Topic> topics = topicService.findByCourseId(courseId);
        Topic modelTopic = new Topic();
        modelTopic.setCourseId(courseId);
        model.addAttribute("course", course);
        model.addAttribute("topics", topics);
        model.addAttribute("topic",modelTopic);
        return "lecturer/course/lecturer_add-topic-to-course";
    }

    @PostMapping({"/lecturer/courses/addTopic"})
    public String addTopic(@ModelAttribute Topic topic, final Model model){
        topic = topicService.addTopic(topic);
        courseService.addTopic(topic);
        Course course = courseService.findByCourseId(topic.getCourseId());
        List<Topic> topics = topicService.findByCourseId(topic.getCourseId());
        Topic modelTopic = new Topic();
        modelTopic.setCourseId(course.getCourseId());
        model.addAttribute("course", course);
        model.addAttribute("topics", topics);
        model.addAttribute("topic",modelTopic);
        return "lecturer/course/lecturer_add-topic-to-course";
    }

    @GetMapping({"/lecturer/courses/updateTopic/{topicId}"})
    public String editTopicProcess(@PathVariable String topicId, final Model model){
        Topic topic = topicService.findById(topicId);
        Course course = courseService.findByCourseId(topic.getCourseId());
        List<Topic> topics = topicService.findByCourseId(course.getCourseId());
        model.addAttribute("course", course);
        model.addAttribute("topics", topics);
        model.addAttribute("topic",topic);
        return "lecturer/course/librarian_update-topic-of-course";
    }


    @PostMapping({"/lecturer/courses/updateTopic/{topicId}"})
    public String editTopic(@PathVariable String topicId ,@ModelAttribute Topic topic, final Model model){
        Topic checkTopicExist = topicService.findById(topicId);
        if(null!=checkTopicExist){
            topicService.updateTopic(topic);
            return "redirect:/lecturer/courses/updateTopic/"+ topicId;
        }return "lecturer/course/lecturer_add-topic-to-course";

    }

    @GetMapping({"/lecturer/courses/deleteTopic/{courseId}/{topicId}"})
    public String deleteTopic( @PathVariable String courseId, @PathVariable String topicId, final Model model){
        Topic topic = topicService.findById(topicId);
        if(null != topic){
            courseService.removeTopic(topic);
            topicService.delete(topicId);
            Course course = courseService.findByCourseId(courseId);
            List<Topic> topics = topicService.findByCourseId(courseId);
            Topic modelTopic = new Topic();
            modelTopic.setCourseId(courseId);
            model.addAttribute("course", course);
            model.addAttribute("topics", topics);
            model.addAttribute("topic", modelTopic);
        }
        return "lecturer/course/lecturer_add-topic-to-course";
    }

    @GetMapping("/lecturer/courses/manage_course/list")
    public String findManageCourses(final Model model){
        Lecturer lecturer = new Lecturer();
        lecturer.setAccountId("65283bfc9bf46d65d5aa3f8f");
        lecturer = lecturerService.findByAccountId(lecturer.getAccountId());
        List<Course> courses = lecturerService.findListManageCourse(lecturer);
        model.addAttribute("courses", courses);
        return "lecturer/course/lecturer_courses";
    }
}
