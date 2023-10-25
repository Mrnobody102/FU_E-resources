package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.dto.AccountDTO;
import fpt.edu.eresourcessystem.enums.AccountEnum;
import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.model.Lecturer;
import fpt.edu.eresourcessystem.model.Librarian;
import fpt.edu.eresourcessystem.model.Student;
import fpt.edu.eresourcessystem.service.*;
import fpt.edu.eresourcessystem.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.List;
import java.util.Objects;

// SỬA ĐƯỜNG LINK HTML
// THÊM ASSMIN
@Controller
@RequestMapping("/admin")
@PropertySource("web-setting.properties")
public class AdminController {

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
    public AdminController(AccountService accountService, LibrarianService librarianService, LecturerService lecturerService, StudentService studentService, CourseService courseService, TopicService topicService, DocumentService documentService) {
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

    @GetMapping({"/courseCreator"})
    public String manageCourseCreators() {
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
        System.out.println(pages);
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
     * @param account
     * @param model
     * @return
     */
    @GetMapping("/accounts/add")
    public String addAccountProcess(@ModelAttribute Account account, final Model model) {
        model.addAttribute("account", Objects.requireNonNullElseGet(account, Account::new));
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
        if (accountService.findByEmail(email) != null) {
            return "redirect:/admin/accounts/add?error";
        }

        accountService.addAccount(accountDTO);
        String role = String.valueOf(accountDTO.getRole());
        switch (role) {
            // Thêm ASSMIN
            case "LIBRARIAN":
                Librarian librarian = new Librarian();
                librarian.setAccountId(accountDTO.getAccountId());
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
        Account account = accountService.findByAccountId(accountId);
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
     * @param account
     * @param model
     * @return
     */
    @PostMapping("/accounts/update")
    public String updateAccount(@ModelAttribute Account account,
                                final Model model) {
        Account checkExist = accountService.findByAccountId(account.getAccountId());
        if (null == checkExist) {
            model.addAttribute("errorMessage", "account not exist.");
            return "exception/404";
        } else {

            Account checkEmailDuplicate = accountService.findByEmail(account.getEmail());
            if (checkEmailDuplicate != null &&
                    !checkExist.getEmail().equalsIgnoreCase(account.getEmail())) {
                String result = "redirect:/admin/accounts/updated/" + account.getAccountId() + "?error";
                return result;
            }
            String role = String.valueOf(account.getRole());
            System.out.println(role);
            switch (role) {
                // Thêm ASSMIN
                case "LIBRARIAN":
                    Librarian librarian = librarianService.findByAccountId(account.getAccountId());
                    if (librarian == null) {
                        librarian = new Librarian();
                        librarian.setAccountId(account.getAccountId());
                        librarianService.addLibrarian(librarian);
                    } else {
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
                    return "redirect:/admin/accounts/updated/" + account.getAccountId() + "?error";
            }
            accountService.updateAccount(account);
            model.addAttribute("account", account);
            model.addAttribute("roles", AccountEnum.Role.values());
            model.addAttribute("campuses", AccountEnum.Campus.values());
            model.addAttribute("genders", AccountEnum.Gender.values());
            model.addAttribute("success", "");
            String result = "redirect:/admin/accounts/updated/" + account.getAccountId() + "?success";
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
            return "redirect:/admin/accounts/list?success";
        }
        return "redirect:/admin/accounts/list?error";
    }

    /**
     * @param accountId
     * @param model
     * @return
     */
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
            return "admin/account/admin_account-detail";
        }

    }
}