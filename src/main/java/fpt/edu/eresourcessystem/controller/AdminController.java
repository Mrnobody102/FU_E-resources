package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.dto.AccountDTO;
import fpt.edu.eresourcessystem.enums.AccountEnum;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.model.*;
import fpt.edu.eresourcessystem.service.*;
import fpt.edu.eresourcessystem.utils.CommonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
@PropertySource("web-setting.properties")
public class AdminController {

    @Value("${page-size}")
    private Integer pageSize;
    private final AccountService accountService;
    private final AdminService adminService;
    private final LibrarianService librarianService;
    private final LecturerService lecturerService;
    private final StudentService studentService;
    private final CourseService courseService;
    private final TopicService topicService;
    private final DocumentService documentService;
    private final FeedbackService feedbackService;

    private  final TrainingTypeService trainingTypeService;

    public AdminController(AccountService accountService, AdminService adminService,
                           LibrarianService librarianService, LecturerService lecturerService,
                           StudentService studentService, CourseService courseService,
                           TopicService topicService, DocumentService documentService,
                           FeedbackService feedbackService, TrainingTypeService trainingTypeService) {
        this.accountService = accountService;
        this.adminService = adminService;
        this.librarianService = librarianService;
        this.lecturerService = lecturerService;
        this.studentService = studentService;
        this.courseService = courseService;
        this.topicService = topicService;
        this.documentService = documentService;
        this.feedbackService = feedbackService;
        this.trainingTypeService = trainingTypeService;
    }

    /*
    DASHBOARD
     */

    /**
     * @return dashboard page
     */
    @GetMapping
    public String getLibrarianDashboard() {
        return "admin/admin_dashboard";
    }

    /*
        ACCOUNTS MANAGEMENT
     */

    /**
     * @return accounts page (no data)
     */
    @GetMapping({"/accounts/list"})
    public String manageAccount() {
        return "admin/account/admin_accounts";
    }

//    @GetMapping({"/courseCreator"})
//    public String manageCourseCreators() {
//        return "admin/account/admin_course_creators";
//    }

    @GetMapping("/course/{courseId}")
    String getCourseByLibrarian(@PathVariable String courseId, final Model model){
        return "admin/account/admin_course_detail";
    }

    /*
    This function to display librarians and created course by that librarians
     */
    @GetMapping("/courseCreator")
    String findCourseByLibrarian(final Model model) {

        List<Account> librarianList = accountService.findAllLibrarian();
        for (int i = 0; i < librarianList.size(); i++) {
            String librarianId = librarianList.get(i).getId();

            Librarian librarian = librarianService.findByAccountId(librarianId);
            if (null == librarian) {
                return "exception/404";
            }
            List<Course> courses = librarian.getCreatedCourses();

            System.out.println(courses.size());
            model.addAttribute("librarianList", librarianList);
            model.addAttribute("librarians", librarian);
            model.addAttribute("courses", courses);
        }

        return "admin/account/admin_course_creators";
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
        model.addAttribute("pages", pages);
        model.addAttribute("totalPage", page.getTotalPages());
        model.addAttribute("accounts", page.getContent());
        model.addAttribute("search", search);
        model.addAttribute("roles", AccountEnum.Role.values());
        model.addAttribute("roleSearch", role);
        model.addAttribute("currentPage", pageIndex);
        return "admin/account/admin_accounts";
    }

    /**
     * @param accountDTO
     * @param model
     * @return
     */
    @GetMapping("/accounts/add")
    public String addAccountProcess(@ModelAttribute AccountDTO accountDTO, final Model model) {
        model.addAttribute("account", Objects.requireNonNullElseGet(accountDTO, Account::new));
        model.addAttribute("roles", AccountEnum.Role.values());
        model.addAttribute("campuses", AccountEnum.Campus.values());
        model.addAttribute("genders", AccountEnum.Gender.values());
        return "admin/account/admin_add-account";
    }

    /**
     * @param accountDTO
     * @return
     */
    @PostMapping("/accounts/add")
    public String addAccount(@ModelAttribute AccountDTO accountDTO) {
        String email = accountDTO.getEmail();
        String role = String.valueOf(accountDTO.getRole());
        if (accountService.findByEmail(email) != null) {
            return "redirect:/admin/accounts/add?error";
        }
        Account account = accountService.addAccount(accountDTO);
        switch (role) {
            case "ADMIN":
                Admin admin = new Admin();
                admin.setAccount(account);
                adminService.addAdmin(admin);
                break;
            case "LIBRARIAN":
                Librarian librarian = new Librarian();
                librarian.setAccount(account);
                librarianService.addLibrarian(librarian);
                break;
            case "STUDENT":
                Student student = new Student();
                student.setAccount(account);
                studentService.addStudent(student);
                break;
            case "LECTURER":
                Lecturer lecturer = new Lecturer();
                lecturer.setAccount(account);
                lecturerService.addLecturer(lecturer);
                break;
            default:
                return "redirect:/admin/accounts/add?error";
        }

        RedirectAttributes attributes = new RedirectAttributesModelMap();
        attributes.addFlashAttribute("account", accountDTO);
        return "redirect:/admin/accounts/add?success";
    }

    /**
     * @param accountId
     * @param model
     * @return
     */
    @GetMapping({"/accounts/{accountId}/update", "/accounts/updated/{accountId}"})
    public String updateAccountProcess(@PathVariable(required = false) String accountId, final Model model) {
        if (null == accountId) {
            accountId = "";
        }
        Account account = accountService.findById(accountId);
        if (null == account) {
            return "exception/404";
        } else {
            if (AccountEnum.Role.LIBRARIAN.equals(account.getRole())) {
                Librarian librarian = librarianService.findByAccountId(accountId);
            }
            model.addAttribute("roles", AccountEnum.Role.values());
            model.addAttribute("campuses", AccountEnum.Campus.values());
            model.addAttribute("genders", AccountEnum.Gender.values());
            model.addAttribute("account", account);
            return "admin/account/admin_update-account";
        }
    }

    /**
     * @param accountDTO
     * @param model
     * @return
     */
    @PostMapping("/accounts/update")
    public String updateAccount(@ModelAttribute AccountDTO accountDTO,
                                       final Model model) {
        Account checkExist = accountService.findById(accountDTO.getId());
        if (null == checkExist) {
            model.addAttribute("errorMessage", "account not exist.");
            return "exception/404";
        } else {
            Account checkEmailDuplicate = accountService.findByEmail(accountDTO.getEmail());
            if (checkEmailDuplicate != null &&
                    !checkExist.getEmail().equalsIgnoreCase(accountDTO.getEmail())) {
                String result = "redirect:/admin/accounts/updated/" + accountDTO.getId() + "?error";
                return result;
            }
            String role = String.valueOf(accountDTO.getRole());
            Account account = new Account(accountDTO);
            System.out.println(role);
            switch (role) {
                case "ADMIN":
                    if (null == adminService.findByAccountId(account.getId())) {
                        Admin admin = new Admin();
                        admin.setAccount(account);
                        adminService.updateAdmin(admin);
                    }
                    break;
                case "LIBRARIAN":
                    Librarian librarian = librarianService.findByAccountId(accountDTO.getId());
                    if (librarian == null) {
                        librarian = new Librarian();
                        librarian.setAccount(account);
                        librarianService.addLibrarian(librarian);
                    } else {
                        librarianService.updateLibrarian(librarian);
                    }
                    break;
                case "STUDENT":
                    if (null == studentService.findByAccountId(account.getId())) {
                        Student student = new Student();
                        student.setAccount(account);
                        studentService.updateStudent(student);
                    }
                    break;
                case "LECTURER":
                    if (null == lecturerService.findByAccountId(account.getId())) {
                        Lecturer lecturer = new Lecturer();
                        lecturer.setAccount(account);
                        lecturerService.updateLecturer(lecturer);
                    }
                    break;
                default:
                    return "redirect:/admin/accounts/updated/" + account.getId() + "?error";
            }
            accountService.updateAccount(account);
            model.addAttribute("account", account);
            model.addAttribute("roles", AccountEnum.Role.values());
            model.addAttribute("campuses", AccountEnum.Campus.values());
            model.addAttribute("genders", AccountEnum.Gender.values());
            model.addAttribute("success", "");
            String result = "redirect:/admin/accounts/updated/" + account.getId() + "?success";
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
    @Transactional
    public String deleteAccount(@PathVariable String accountId) {
        Account foundAccount = accountService.findById(accountId);
        AccountEnum.Role role = foundAccount.getRole();
        if (null != foundAccount) {
            // SOFT DELETE

            switch (role) {
                case ADMIN:
                    Admin admin = adminService.findByAccountId(accountId);
                    admin.setDeleteFlg(CommonEnum.DeleteFlg.DELETED);
                    adminService.updateAdmin(admin);
                    break;
                case LIBRARIAN:
                    Librarian librarian = librarianService.findByAccountId(accountId);
                    librarian.setDeleteFlg(CommonEnum.DeleteFlg.DELETED);
                    librarianService.updateLibrarian(librarian);

                    break;
                case LECTURER:
                    Lecturer lecturer = lecturerService.findByAccountId(accountId);
                    lecturer.setDeleteFlg(CommonEnum.DeleteFlg.DELETED);
                    lecturerService.updateLecturer(lecturer);
                    // Xóa quyền quản lý môn học + ghi log
                    // Xóa mềm topic đã tạo
                    // Xóa mềm resource type đã tạo
                    // Xóa mềm tài liệu đã đăng

                    break;
                case STUDENT:
                    Student student = studentService.findByAccountId(accountId);
                    student.setDeleteFlg(CommonEnum.DeleteFlg.DELETED);
                    studentService.updateStudent(student);
                    // Xóa mềm note on document
                    // Xóa mềm question của student
                    // Xóa mềm saved course
                    // Xóa mềm saved document
                    // Xóa mềm student self note

                    break;
            }
            foundAccount.setDeleteFlg(CommonEnum.DeleteFlg.DELETED);
            accountService.updateAccount(foundAccount);

            return "redirect:/admin/accounts/list/1?success";
        }
        return "redirect:/admin/accounts" + accountId + "/update";
    }

    @GetMapping("/feedbacks/list/{pageIndex}")
    String showFeedbacksByPage(@PathVariable Integer pageIndex,
                             @RequestParam(required = false, defaultValue = "") String search,
                             final Model model, HttpServletRequest request) {
        Page<Course> page;
//        page = courseService.findByCodeOrNameOrDescription(search, search, search, pageIndex, pageSize);
//        List<Integer> pages = CommonUtils.pagingFormat(page.getTotalPages(), pageIndex);
//        model.addAttribute("pages", pages);
//        model.addAttribute("totalPage", page.getTotalPages());
//        model.addAttribute("courses", page.getContent());
//        model.addAttribute("search", search);
//        model.addAttribute("currentPage", pageIndex);
        return "librarian/course/librarian_courses";
    }

    @GetMapping({"/feedbacks"})
    public String showFeedbacks(final Model model) {
        List<Feedback> feedbacks = feedbackService.findAll();
        model.addAttribute("feedbacks", feedbacks);
        return "admin/feedback/admin_feedbacks";
//        return  "librarian/course/detailCourseTest";
    }


    @GetMapping("/trainingtypes/add")
    public String showAddForm(Model model) {
        List<Course> allCourses = courseService.findAll(); // Retrieve all available courses
        model.addAttribute("allCourses", allCourses);
        model.addAttribute("trainingType", new TrainingType());
        return "admin/training_type/admin_training-type-add";
    }

    @PostMapping("/trainingtypes/add")
    public String addTrainingType(@ModelAttribute("trainingtype") @Valid TrainingType trainingType,
                                  BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/training_type/admin_training-type-add";
        }
        try {
            // Save the new training type using the service layer
            trainingTypeService.save(trainingType);
            redirectAttributes.addFlashAttribute("success", "Training type saved successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "There was an error saving the training type.");
        }

        return "redirect:/admin/trainingtypes/list";
    }

    @GetMapping("/trainingtypes/list")
    public String listTrainingTypes(Model model) {
        model.addAttribute("trainingTypes", trainingTypeService.findAll());
        return "admin/training_type/admin_training-type"; // Thymeleaf template for listing training types
    }

    @GetMapping("/trainingtypes/{id}")
    public String viewTrainingTypeDetail(@PathVariable("id") String id, Model model, RedirectAttributes redirectAttributes) {
        Optional<TrainingType> trainingTypeOptional = trainingTypeService.findById(id);
        if (trainingTypeOptional.isPresent()) {
            model.addAttribute("trainingType", trainingTypeOptional.get());
            return "admin/training_type/admin_training-type-detail"; // Thymeleaf template for the details view
        } else {
            redirectAttributes.addFlashAttribute("error", "Training type not found!");
            return "redirect:/admin/trainingtypes/list";
        }
    }

}