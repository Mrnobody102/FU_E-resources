package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.enums.AccountEnum;
import fpt.edu.eresourcessystem.enums.CourseEnum;
import fpt.edu.eresourcessystem.model.*;
import fpt.edu.eresourcessystem.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.List;

@Controller
@RequestMapping("/librarian")
@PropertySource("web-setting.properties")
public class LibrarianController {

    @Value("${page-size}")
    private Integer pageSize;
    private final AccountService accountService;
    private final LibrarianService librarianService;
    private final LecturerService lecturerService;
    private final StudentService studentService;
    private final CourseService courseService;
    private final TopicService topicService;

    public LibrarianController(AccountService accountService, LibrarianService librarianService, LecturerService lecturerService, StudentService studentService, CourseService courseService, TopicService topicService) {
        this.accountService = accountService;
        this.librarianService = librarianService;
        this.lecturerService = lecturerService;
        this.studentService = studentService;
        this.courseService = courseService;
        this.topicService = topicService;
    }

    /*
    DASHBOARD
     */

    /**
     * @return dashboard page
     */
    @GetMapping
    public String getLibrarianDashboard() {
        return "librarian/librarian_dashboard";
    }

    /*
    ACCOUNTS MANAGEMENT
     */

    /**
     * @return accounts page (no data)
     */
    @GetMapping({"/accounts/list"})
    public String manageAccount() {
        return "librarian/account/librarian_accounts";
    }

    @GetMapping("/accounts/list/{pageIndex}")
    String findAccountByPage(@PathVariable Integer pageIndex,
                             @RequestParam(required = false, defaultValue = "") String search,
                             @RequestParam(required = false, defaultValue = "all") String role,
                             final Model model, HttpServletRequest request) {
        Page<Account> page;
        if(null==role || "all".equals(role)){
            page = accountService.findByUsernameLikeOrEmailLike(search, search, pageIndex, pageSize);
        }else {
            page = accountService.findByRoleAndUsernameLikeOrEmailLike( role, search, search, pageIndex, pageSize);
        }
        model.addAttribute("totalPage", page.getTotalPages());
        model.addAttribute("accounts", page.getContent());
        model.addAttribute("search", search);
        model.addAttribute("roles", AccountEnum.Role.values());
        model.addAttribute("roleSearch", role);
        model.addAttribute("currentPage", pageIndex);
        return "librarian/account/librarian_accounts";
    }

    @GetMapping("/accounts/add")
    public String addAccountProcess(@ModelAttribute Account account, final Model model) {
        if (null != account) {
            model.addAttribute("account", account);
        } else {
            model.addAttribute("account", new Account());
        }
        model.addAttribute("roles", AccountEnum.Role.values());
        model.addAttribute("campuses", AccountEnum.Campus.values());
        model.addAttribute("genders", AccountEnum.Gender.values());
        return "librarian/account/librarian_add-account";
    }

    @PostMapping("/accounts/add")
    public String addAccount(@ModelAttribute Account account, @RequestParam(name = "isAdmin", required = false) boolean isAdmin) {
        Account checkExist = accountService.findByEmail(account.getEmail());
        if (checkExist != null) {
            return "redirect:/librarian/accounts/add?error";
        }
        accountService.addAccount(account);
        String role = String.valueOf(account.getRole());
        switch (role) {
            case "LIBRARIAN":
                Librarian librarian = new Librarian();
                librarian.setAccountId(account.getAccountId());
                librarian.setFlagAdmin(isAdmin);
                librarianService.addLibrarian(librarian);
                break;
            case "STUDENT":
                Student student = new Student();
                student.setAccountId(account.getAccountId());
                studentService.addStudent(student);
                break;
            case "LECTURER":
                Lecturer lecturer = new Lecturer();
                lecturer.setAccountId(account.getAccountId());
                lecturerService.addLecturer(lecturer);
                break;
            default:
                return "redirect:/librarian/accounts/add?error";
        }

        RedirectAttributes attributes = new RedirectAttributesModelMap();
        attributes.addFlashAttribute("account", account);
        return "redirect:/librarian/accounts/add?success";
    }

    @GetMapping({"/accounts/{accountId}/update", "/update"})
    public String updateAccountProcess(@PathVariable(required = false) String accountId, final Model model) {
        if (null == accountId) {
            accountId = "";
        }
        Account account = accountService.findByAccountId(accountId);
        if (null == account) {
            return "redirect:librarian/accounts/update?error";
        } else {
            model.addAttribute("roles", AccountEnum.Role.values());
            model.addAttribute("campuses", AccountEnum.Campus.values());
            model.addAttribute("genders", AccountEnum.Gender.values());
            model.addAttribute("account", account);
            return "librarian/account/librarian_update-account";
        }

    }

    @PostMapping("/accounts/update")
    public String updateAccount(@ModelAttribute Account account, final Model model) {
        Account checkExist = accountService.findByAccountId(account.getAccountId());
        if (null == checkExist) {
            model.addAttribute("errorMessage", "account not exist.");
            return "exception/404";
        } else {

            Account checkEmailDuplicate = accountService.findByEmail(account.getEmail());
            if (checkEmailDuplicate != null &&
                    !checkExist.getEmail().toLowerCase().equals(account.getEmail())) {
                return "redirect:/librarian/courses/update?error";
            }
            String role = String.valueOf(account.getRole());
            System.out.println(role);
            switch (role) {
                case "LIBRARIAN":
                    if (null == librarianService.findByAccountId(account.getAccountId())) {
                        Librarian librarian = new Librarian();
                        librarian.setAccountId(account.getAccountId());
                        librarian.setFlagAdmin(false); // sửa sau
                        librarianService.addLibrarian(librarian);
                    }
                    break;
                case "STUDENT":
                    if (null == studentService.findByAccountId(account.getAccountId())) {
                        Student student = new Student();
                        student.setAccountId(account.getAccountId());
                        studentService.addStudent(student);
                    }
                    break;
                case "LECTURER":
                    if (null == lecturerService.findByAccountId(account.getAccountId())) {
                        Lecturer lecturer = new Lecturer();
                        lecturer.setAccountId(account.getAccountId());
                        lecturerService.addLecturer(lecturer);
                    }
                    break;
                default:
                    return "redirect:/librarian/accounts/update?error";
            }
            accountService.updateAccount(account);
            model.addAttribute("account", account);
            model.addAttribute("roles", AccountEnum.Role.values());
            model.addAttribute("campuses", AccountEnum.Campus.values());
            model.addAttribute("genders", AccountEnum.Gender.values());
            model.addAttribute("success", "");
            return "librarian/account/librarian_update-account";
        }
    }

    /**
     * Delete an account by accountId
     *
     * @param accountId accountId that delete
     * @return list accounts after delete
     */
    @GetMapping("/accounts/{accountId}/delete")
    public String deleteAccount(@PathVariable String accountId) {
        Account check = accountService.findByAccountId(accountId);
        System.out.println(check);
        if (null != check) {
            accountService.delete(check);
            return "redirect:/librarian/accounts/list?success";
        }
        return "redirect:/librarian/accounts/list?error";
    }


    @GetMapping({"accounts/{accountId}"})
    public String getAccountDetail(@PathVariable(required = false) String accountId, final Model model) {
        if (null == accountId) {
            accountId = "";
        }
        Account account = accountService.findByAccountId(accountId);
        if (null == account) {
            return "exception/404";
        } else {
            model.addAttribute("account", account);
            return "librarian/account/librarian_account-detail";
        }

    }


    /*
        COURSES MANAGEMENT
     */

    @GetMapping({"/courses/add"})
    public String addCourseProcess(final Model model) {
        List<Account> lecturers = accountService.findAllLecturer();
        model.addAttribute("course", new Course());
        model.addAttribute("lecturers", lecturers);
        model.addAttribute("majors", CourseEnum.Major.values());
        return "librarian/course/librarian_add-course";
    }

    @PostMapping("/courses/add")
    public String addCourse(@ModelAttribute Course course, @RequestParam(required = false) String topic, @RequestParam(required = false) String lecturer) {

        Course checkExist = courseService.findByCourseCode(course.getCourseCode());
        if (null == checkExist) {
            courseService.addCourse(course);
            return "redirect:/librarian/courses/add?success";
        } else return "redirect:/librarian/courses/add?error";

    }

    @GetMapping({"/courses/update/{courseId}", "/update"})
    public String updateCourseProcess(@PathVariable(required = false) String courseId, final Model model) {
        if (null == courseId) {
            courseId = "";
        }
        Course course = courseService.findByCourseId(courseId);
        if (null == course) {
            return "redirect:librarian/courses/update?error";
        } else {
            List<Account> lecturers = accountService.findAllLecturer();
            model.addAttribute("lecturers", lecturers);
            model.addAttribute("course", course);
            model.addAttribute("majors", CourseEnum.Major.values());
            return "librarian/course/librarian_update-course";
        }
    }

    @PostMapping("/courses/update")
    public String updateCourse(@ModelAttribute Course course, final Model model) {
        Course checkExist = courseService.findByCourseId(course.getCourseId());
        if (null == checkExist) {
            model.addAttribute("errorMessage", "course not exist.");
            return "exception/404";
        } else {
            Course checkCodeDuplicate = courseService.findByCourseCode(course.getCourseCode());
            if (checkCodeDuplicate != null &&
                    !checkExist.getCourseCode().toLowerCase().equals(course.getCourseCode())) {
//                return "redirect:/librarian/courses/update?error";
            }
            courseService.updateCourse(course);
            model.addAttribute("course", course);
            List<Account> lecturers = accountService.findAllLecturer();
            model.addAttribute("lecturers", lecturers);
            model.addAttribute("success", "");
            return "librarian/course/librarian_update-course";
        }
    }

    @GetMapping({"/courses/list"})
    public String showCourses() {
        return "librarian/course/librarian_courses";
    }

    @GetMapping("/courses/list/{pageIndex}")
    String showCoursesByPage(@PathVariable Integer pageIndex,
                             @RequestParam(required = false, defaultValue = "") String search,
                             @RequestParam(required = false, defaultValue = "all") String major,
                             final Model model, HttpServletRequest request) {
        Page<Course> page;
        if(null==major || "all".equals(major)){
            page = courseService.findByCourseCodeLikeOrCourseNameLikeOrDescriptionLike(search, search, search, pageIndex, pageSize);
        }else {
            page = courseService.filterMajor(major, search, search, search, pageIndex, pageSize);
        }
        model.addAttribute("totalPage", page.getTotalPages());
        model.addAttribute("courses", page.getContent());
        model.addAttribute("search", search);
        model.addAttribute("currentPage", pageIndex);
        model.addAttribute("majorSearch", major);
        model.addAttribute("majors", CourseEnum.Major.values());
        return "librarian/course/librarian_courses";
    }

    @GetMapping({"/courses/{courseId}"})
    public String showCourseDetail(@PathVariable String courseId, final Model model) {
        Course course = courseService.findByCourseId(courseId);
        List<Topic> topics = topicService.findByCourseId(courseId);
        model.addAttribute("course", course);
        model.addAttribute("topics", topics);
        return "librarian/course/librarian_course-detail";
    }


    @GetMapping("/{courseId}/delete")
    public String delete(@PathVariable String courseId) {
        Course checkExist = courseService.findByCourseId(courseId);
//        System.out.println(checkExist);
        if (null != checkExist) {
            for (String topicId : checkExist.getTopics()) {
                topicService.delete(topicId);
            }
            courseService.delete(checkExist);
            return "redirect:/librarian/courses/list?success";
        }
        return "redirect:/librarian/courses/list?error";
    }

    @GetMapping({"/addLecturers/{courseId}"})
    public String addLecturersProcess(@PathVariable String courseId, final Model model) {
        Course course = courseService.findByCourseId(courseId);
        List<Lecturer> lecturers = lecturerService.findByCourseId(courseId);
        List<Account> accounts = accountService.findAllLecturer();
        model.addAttribute("course", course);
        model.addAttribute("lecturers", lecturers);
        model.addAttribute("accounts",accounts);
        return "librarian/course/librarian_add-lecturer-to-course";
    }

    @PostMapping({"/addLecturers/{courseId}"})
    public String addLecturers(@PathVariable String courseId, @RequestParam String accountId, final Model model) {
        List<Lecturer> courseLecturers = lecturerService.findByCourseId(courseId);
        List<Account> lecturers = accountService.searchLecturer("");
        model.addAttribute("courseLecturers", courseLecturers);
        model.addAttribute("lecturers", lecturers);
        return "librarian/course/librarian_add-lecturer-to-course";
    }


    @GetMapping({"/courses/updateLecturers/{courseId}"})
    public String updateLecturersProcess(@PathVariable String courseId, @RequestParam String search, final Model model) {
        List<Lecturer> courseLecturers = lecturerService.findByCourseId(courseId);
        List<Account> lecturers = accountService.searchLecturer(search);
        model.addAttribute("courseLecturers", courseLecturers);
        model.addAttribute("lecturers", lecturers);
        return "librarian/course/librarian_add-lecturer-to-course";
    }

    @PostMapping({"/courses/{courseId}/addLecturers"})
    public String addLecturersProcess(@PathVariable String courseId, @RequestParam String accountId, final Model model) {
        List<Lecturer> courseLecturers = lecturerService.findByCourseId(courseId);
        List<Account> lecturers = accountService.searchLecturer("");
        model.addAttribute("courseLecturers", courseLecturers);
        model.addAttribute("lecturers", lecturers);
        return "librarian/course/librarian_add-lecturer-to-course";
    }

    @GetMapping({"/courses/addTopic/{courseId}", "/courses/topics/{courseId}"})
    public String addTopicProcess(@PathVariable String courseId, final Model model) {
        Course course = courseService.findByCourseId(courseId);
        List<Topic> topics = topicService.findByCourseId(courseId);
        model.addAttribute("course", course);
        model.addAttribute("topics", topics);
        Topic topic = new Topic();
        topic.setCourseId(courseId);
        model.addAttribute("topic", topic);
        return "librarian/course/librarian_add-topic-to-course";
    }

    @PostMapping({"/courses/addTopic"})
    public String addTopic(@ModelAttribute Topic topic, final Model model) {
        System.out.println(topic);
        topic = topicService.addTopic(topic);
        courseService.addTopic(topic);
        Course course = courseService.findByCourseId(topic.getCourseId());
        List<Topic> topics = topicService.findByCourseId(topic.getCourseId());
        model.addAttribute("course", course);
        model.addAttribute("topics", topics);
        Topic modelTopic = new Topic();
        modelTopic.setCourseId(course.getCourseId());
        model.addAttribute("topic", modelTopic);
        return "librarian/course/librarian_add-topic-to-course";
    }

    @GetMapping({"/courses/updateTopic/{topicId}"})
    public String editTopicProcess(@PathVariable String topicId, final Model model) {
        Topic topic = topicService.findById(topicId);
        Course course = courseService.findByCourseId(topic.getCourseId());
        List<Topic> topics = topicService.findByCourseId(course.getCourseId());
        model.addAttribute("course", course);
        model.addAttribute("topics", topics);
        model.addAttribute("topic", topic);
        return "librarian/course/librarian_update-topic-of-course";
    }


    @PostMapping({"/courses/updateTopic/{topicId}"})
    public String editTopic(@PathVariable String topicId, @ModelAttribute Topic topic, final Model model) {
        Topic checkTopicExist = topicService.findById(topicId);
        if (null != checkTopicExist) {
            topicService.updateTopic(topic);
            return "redirect:/librarian/courses/updateTopic/" + topicId;
        }
        return "librarian/course/librarian_add-topic-to-course";

    }

    @GetMapping({"/courses/deleteTopic/{courseId}/{topicId}"})
    public String deleteTopic(@PathVariable String courseId, @PathVariable String topicId, final Model model) {
        Topic topic = topicService.findById(topicId);
        if (null != topic) {
            courseService.removeTopic(topic);
            topicService.delete(topicId);
            Course course = courseService.findByCourseId(courseId);
            List<Topic> topics = topicService.findByCourseId(courseId);
            Topic modelTopic = new Topic();
            modelTopic.setCourseId(courseId);
            model.addAttribute("course", course);
            model.addAttribute("topics", topics);
            model.addAttribute("topic", modelTopic);
            return "librarian/course/librarian_add-topic-to-course";
        }
        return "librarian/course/librarian_add-topic-to-course";
    }
}
