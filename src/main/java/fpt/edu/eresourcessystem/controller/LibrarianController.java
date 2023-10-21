package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.dto.AccountDTO;
import fpt.edu.eresourcessystem.enums.AccountEnum;
import fpt.edu.eresourcessystem.enums.CourseEnum;
import fpt.edu.eresourcessystem.model.*;
import fpt.edu.eresourcessystem.service.*;
import fpt.edu.eresourcessystem.utils.CommonUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

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
    private final DocumentService documentService;

    @Autowired
    public LibrarianController(AccountService accountService, LibrarianService librarianService, LecturerService lecturerService, StudentService studentService, CourseService courseService, TopicService topicService, DocumentService documentService) {
        this.accountService = accountService;
        this.librarianService = librarianService;
        this.lecturerService = lecturerService;
        this.studentService = studentService;
        this.courseService = courseService;
        this.topicService = topicService;
        this.documentService = documentService;
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
                             final Model model) {
        Page<Account> page;
        if (null == role || "all".equals(role)) {
            page = accountService.findByUsernameLikeOrEmailLike(search, search, pageIndex, pageSize);
        } else {
            page = accountService.findByRoleAndUsernameLikeOrEmailLike(role, search, search, pageIndex, pageSize);
        }
        List<Integer> pages = CommonUtils.pagingFormat(page.getTotalPages(), pageIndex);
        System.out.println(pages);
        model.addAttribute("pages", pages);
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
        model.addAttribute("account", Objects.requireNonNullElseGet(account, Account::new));
        model.addAttribute("roles", AccountEnum.Role.values());
        model.addAttribute("campuses", AccountEnum.Campus.values());
        model.addAttribute("genders", AccountEnum.Gender.values());
        return "librarian/account/librarian_add-account";
    }

    @PostMapping("/accounts/add")
    public String addAccount(@ModelAttribute AccountDTO accountDTO,
                             @RequestParam(name = "isAdmin", required = false) boolean isAdmin) {
        String email = accountDTO.getEmail();
        if (accountService.findByEmail(email) != null) {
            return "redirect:/librarian/accounts/add?error";
        }

        accountService.addAccount(accountDTO);
        String role = String.valueOf(accountDTO.getRole());
        switch (role) {
            case "LIBRARIAN":
                Librarian librarian = new Librarian();
                librarian.setAccountId(accountDTO.getAccountId());
                librarian.setFlagAdmin(isAdmin);
                librarianService.addLibrarian(librarian);
                break;
            case "STUDENT":
                Student student = new Student();
                student.setAccountId(accountDTO.getAccountId());
                studentService.addStudent(student);
                break;
            case "LECTURER":
                Lecturer lecturer = new Lecturer();
                lecturer.setAccountId(accountDTO.getAccountId());
                lecturerService.addLecturer(lecturer);
                break;
            default:
                return "redirect:/librarian/accounts/add?error";
        }

        RedirectAttributes attributes = new RedirectAttributesModelMap();
        attributes.addFlashAttribute("account", accountDTO);
        return "redirect:/librarian/accounts/add?success";
    }

    @GetMapping({"/accounts/{accountId}/update", "/accounts/updated/{accountId}"})
    public String updateAccountProcess(@PathVariable(required = false) String accountId, final Model model) {
        if (null == accountId) {
            accountId = "";
        }
        Account account = accountService.findByAccountId(accountId);
        if (null == account) {
            return "exception/404";
        } else {
            if (AccountEnum.Role.LIBRARIAN.equals(account.getRole())) {
                Librarian librarian = librarianService.findByAccountId(accountId);
                boolean isAdmin = false;
                if(librarian != null) {
                    isAdmin = librarian.isFlagAdmin();
                }
                model.addAttribute("isAdmin", isAdmin);
            }
            model.addAttribute("roles", AccountEnum.Role.values());
            model.addAttribute("campuses", AccountEnum.Campus.values());
            model.addAttribute("genders", AccountEnum.Gender.values());
            model.addAttribute("account", account);
            return "librarian/account/librarian_update-account";
        }

    }

    @PostMapping("/accounts/update")
    public String updateAccount(@ModelAttribute Account account,
                                @RequestParam(name = "isAdmin", required = false) boolean isAdmin,
                                final Model model) {
        Account checkExist = accountService.findByAccountId(account.getAccountId());
        if (null == checkExist) {
            model.addAttribute("errorMessage", "account not exist.");
            return "exception/404";
        } else {

            Account checkEmailDuplicate = accountService.findByEmail(account.getEmail());
            if (checkEmailDuplicate != null &&
                    !checkExist.getEmail().equalsIgnoreCase(account.getEmail())) {
                String result = "redirect:/librarian/accounts/updated/" + account.getAccountId() + "?error";
                return result;
            }
            String role = String.valueOf(account.getRole());
            System.out.println(role);
            switch (role) {
                case "LIBRARIAN":
                    Librarian librarian = librarianService.findByAccountId(account.getAccountId());
                    if (librarian == null) {
                        librarian = new Librarian();
                        librarian.setAccountId(account.getAccountId());
                        librarian.setFlagAdmin(isAdmin);
                        librarianService.addLibrarian(librarian);
                    } else {
                        librarian.setFlagAdmin(isAdmin);
                        librarianService.updateLibrarian(librarian);
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
                    return "redirect:/librarian/accounts/updated/" + account.getAccountId() + "?error";
            }
            accountService.updateAccount(account);
            model.addAttribute("account", account);
            model.addAttribute("roles", AccountEnum.Role.values());
            model.addAttribute("campuses", AccountEnum.Campus.values());
            model.addAttribute("genders", AccountEnum.Gender.values());
            model.addAttribute("success", "");
            String result = "redirect:/librarian/accounts/updated/" + account.getAccountId() + "?success";
            return result;
        }
    }

    /**
     * Delete an account by accountId
     *
     * @param accountId id of account that delete
     * @return list of accounts after delete
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
    public String addCourse(final Model model) {
        List<Account> lecturers = accountService.findAllLecturer();
        model.addAttribute("course", new Course());
        model.addAttribute("lecturers", lecturers);
        model.addAttribute("majors", CourseEnum.Major.values());
        return "librarian/course/librarian_add-course";
    }

    @PostMapping("/courses/add")
    public String addCourseProcess(@ModelAttribute Course course, @RequestParam(required = false) String topic, @RequestParam(required = false) String lecturer) {

        Course checkExist = courseService.findByCourseCode(course.getCourseCode());
        if (null == checkExist) {
            courseService.addCourse(course);
            return "redirect:/librarian/courses/add?success";
        } else return "redirect:/librarian/courses/add?error";

    }

    @GetMapping({"/courses/update/{courseId}"})
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
        if (null == major || "all".equals(major)) {
            page = courseService.findByCodeOrNameOrDescription(search, search, search, pageIndex, pageSize);
        } else {
            page = courseService.filterMajor(major, search, search, search, pageIndex, pageSize);
        }
        List<Integer> pages = CommonUtils.pagingFormat(page.getTotalPages(), pageIndex);
        model.addAttribute("pages", pages);
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


    @GetMapping("/courses/{courseId}/delete")
    public String deleteCourse(@PathVariable String courseId) {
        Course checkExist = courseService.findByCourseId(courseId);
        if (null != checkExist) {
            List<String> topics = checkExist.getTopics();
            if (null != topics) {
                for (String topicId : topics) {
                    topicService.delete(topicId);
                }
            }
            courseService.delete(checkExist);
            return "redirect:/librarian/courses/list?success";
        }
        return "redirect:/librarian/courses/list?error";
    }

    @GetMapping({"/courses/{courseId}/addLecturers"})
    public String addLecturersProcess(@PathVariable String courseId, final Model model) {
        Course course = courseService.findByCourseId(courseId);
        List<Lecturer> lecturers = lecturerService.findByCourseId(courseId);
        List<Account> accounts = accountService.findAllLecturer();
        model.addAttribute("course", course);
        model.addAttribute("lecturers", lecturers);
        model.addAttribute("accounts", accounts);
        return "librarian/course/librarian_add-lecturer-to-course";
    }

    @PostMapping({"/courses/{courseId}/addLecturers"})
    public String addLecturers(@PathVariable String courseId, @RequestParam String accountId, final Model model) {
        List<Lecturer> courseLecturers = lecturerService.findByCourseId(courseId);
        List<Account> lecturers = accountService.searchLecturer("");
        model.addAttribute("courseLecturers", courseLecturers);
        model.addAttribute("lecturers", lecturers);
        return "librarian/course/librarian_add-lecturer-to-course";
    }


    @GetMapping({"/courses/{courseId}/updateLecturers"})
    public String updateLecturersProcess(@PathVariable String courseId, @RequestParam String search, final Model model) {
        List<Lecturer> courseLecturers = lecturerService.findByCourseId(courseId);
        List<Account> lecturers = accountService.searchLecturer(search);
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


    /*
        DOCUMENTS
     */

    @GetMapping({"/documents/list"})
    public String showDocuments() {
        return "librarian/document/librarian_documents";
    }

    @GetMapping("/documents/list/{pageIndex}")
    String showDocumentsByPage(@PathVariable Integer pageIndex,
                               @RequestParam(required = false, defaultValue = "") String search,
                               @RequestParam(required = false, defaultValue = "all") String course,
                               @RequestParam(required = false, defaultValue = "all") String topic,
                               final Model model, HttpServletRequest request) {
        Page<Document> page = documentService.filterAndSearchDocument(course, topic, search, search, pageIndex, pageSize);

        List<Integer> pages = CommonUtils.pagingFormat(page.getTotalPages(), pageIndex);
        model.addAttribute("pages", pages);
        model.addAttribute("totalPage", page.getTotalPages());
        model.addAttribute("currentPage", pageIndex);
        model.addAttribute("documents", page.getContent());
        model.addAttribute("search", search);
        model.addAttribute("selectedCourse", course);
        model.addAttribute("courses", courseService.findAll());
        return "librarian/document/librarian_documents";
    }

    @GetMapping({"/documents/{documentId}"})
    public String showDocumentDetail(@PathVariable String documentId, final Model model) {
        Document document = documentService.findById(documentId);
        model.addAttribute("document", document);
        return "librarian/document/librarian_document-detail";
    }

    @GetMapping({"/documents/add"})
    public String addDocument(final Model model) {
        model.addAttribute("document", new Document());
        model.addAttribute("courses", courseService.findAll());
        return "librarian/document/librarian_add-document";
    }

    @PostMapping("/documents/add")
    public String addDocumentProcess(@ModelAttribute Document document,
                                     @RequestParam("file") MultipartFile file) throws IOException {
        // thêm check
        String id = documentService.addFile(file);
        documentService.addDocument(document, id);
        return "redirect:/librarian/documents/add?success";
    }

    @GetMapping({"/documents/update/{documentId}"})
    public String updateDocumentProcess(@PathVariable(required = false) String documentId, final Model model) {
        if (null == documentId) {
            documentId = "";
        }
        Document document = documentService.findById(documentId);
        if (null == document) {
            return "redirect:librarian/documents/update?error";
        } else {
            model.addAttribute("document", new Document());
            model.addAttribute("courses", courseService.findAll());
            return "librarian/document/librarian_update-document";
        }
    }

//    @PostMapping("/documents/update")
//    public String updateDocument(@ModelAttribute Document document, final Model model) {
//        Document checkExist = documentService.findByDocumentId(document.getDocumentId());
//        if (null == checkExist) {
//            model.addAttribute("errorMessage", "document not exist.");
//            return "exception/404";
//        } else {
//            Document checkCodeDuplicate = documentService.findByDocumentCode(document.getDocumentCode());
//            if (checkCodeDuplicate != null &&
//                    !checkExist.getDocumentCode().toLowerCase().equals(document.getDocumentCode())) {
////                return "redirect:/librarian/documents/update?error";
//            }
//            documentService.updateDocument(document);
//            model.addAttribute("document", document);
//            List<Account> lecturers = accountService.findAllLecturer();
//            model.addAttribute("lecturers", lecturers);
//            model.addAttribute("success", "");
//            return "librarian/document/librarian_update-document";
//        }
//    }
//
//    @GetMapping("/documents/{documentId}/delete")
//    public String deleteDocument(@PathVariable String documentId) {
//        Document checkExist = documentService.findByDocumentId(documentId);
//        if (null != checkExist) {
//            List<String> topics = checkExist.getTopics();
//            if (null != topics) {
//                for (String topicId : topics) {
//                    topicService.delete(topicId);
//                }
//            }
//            documentService.delete(checkExist);
//            return "redirect:/librarian/documents/list?success";
//        }
//        return "redirect:/librarian/documents/list?error";
//    }
}
