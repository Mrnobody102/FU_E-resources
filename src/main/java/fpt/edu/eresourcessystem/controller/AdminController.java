package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.dto.AccountDTO;
import fpt.edu.eresourcessystem.enums.AccountEnum;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.model.*;
import fpt.edu.eresourcessystem.service.*;
import fpt.edu.eresourcessystem.utils.CommonUtils;
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

    public AdminController(AccountService accountService, AdminService adminService, LibrarianService librarianService, LecturerService lecturerService, StudentService studentService, CourseService courseService, TopicService topicService, DocumentService documentService) {
        this.accountService = accountService;
        this.adminService = adminService;
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
    public String updateAccount(@PathVariable(required = false) String accountId, final Model model) {
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
    public String updateAccountProcess(@ModelAttribute AccountDTO accountDTO,
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
                        Admin admin = new Admin();
                        admin.setAccount(account);
                        adminService.updateAdmin(admin);
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
//                    if (null == studentService.findByAccountId(account.getId())) {
                        Student student = new Student();
                        student.setAccount(account);
                        studentService.addStudent(student);
//                    }
                    break;
                case "LECTURER":
                    if (null == lecturerService.findByAccountId(account.getId())) {
                        Lecturer lecturer = new Lecturer();
                        lecturer.setAccount(account);
                        lecturerService.addLecturer(lecturer);
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
    public String deleteAccount(@PathVariable String accountId) {
        Account foundAccount = accountService.findById(accountId);
        if (null != foundAccount) {
            // SOFT DELETE
            foundAccount.setDeleteFlg(CommonEnum.DeleteFlg.DELETED);
            accountService.updateAccount(foundAccount);
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
        Account account = accountService.findById(accountId);
        if (null == account) {
            return "exception/404";
        } else {
            model.addAttribute("account", account);
            return "admin/account/admin_account-detail";
        }

    }
}