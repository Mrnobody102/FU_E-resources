package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.dto.AccountDTO;
import fpt.edu.eresourcessystem.dto.CourseDTO;
import fpt.edu.eresourcessystem.dto.DocumentDTO;
import fpt.edu.eresourcessystem.enums.AccountEnum;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.enums.CourseEnum;
import fpt.edu.eresourcessystem.enums.DocumentEnum;
import fpt.edu.eresourcessystem.model.*;
import fpt.edu.eresourcessystem.service.*;
import fpt.edu.eresourcessystem.utils.CommonUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.bson.types.ObjectId;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/librarian")
@PropertySource("web-setting.properties")
public class LibrarianController {

    private final JavaMailSender javaMailSender;

    //    @Value("${page-size}")
    private static final Integer pageSize = 5;
    private final AccountService accountService;
    private final LibrarianService librarianService;
    private final LecturerService lecturerService;
    private final StudentService studentService;
    private final CourseService courseService;
    private final TopicService topicService;
    private final ResourceTypeService resourceTypeService;
    private final DocumentService documentService;

    private final LecturerCourseService lecturerCourseService;

    private final TrainingTypeService trainingTypeService;

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
        COURSES MANAGEMENT
     */

    /**
     * @param model
     * @return
     */
    @GetMapping({"/courses/add"})
    public String addCourse(final Model model) {
        List<TrainingType> trainingTypes = trainingTypeService.findAll();
        model.addAttribute("trainingTypes", trainingTypes);
        model.addAttribute("course", new Course());
        return "librarian/course/librarian_add-course";
    }

    /**
     * @param courseDTO
     * @param lecturer
     * @return
     */
    @PostMapping("/courses/add")
    public String addCourseProcess(@ModelAttribute CourseDTO courseDTO,
                                   @RequestParam(value = "lecturer") String lecturer) {
        Course course = new Course(courseDTO, CourseEnum.Status.HIDE);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return "exception/404";
        }
        String currentPrincipalName = authentication.getName();
        Librarian librarian = librarianService.findByAccountId(accountService.findByEmail(currentPrincipalName).getId());
        course.setLibrarian(librarian);

        // check course code duplicate
        Course checkExist = courseService.findByCourseCode(course.getCourseCode());

        if (null == checkExist) {
            // check lecturer param exist
            if (lecturer != null && !"".equals(lecturer.trim())) {
                // get account by EMAIL
                Account account = accountService.findByEmail(lecturer);
                if (null != account) {
                    // get lecturer by account
                    Lecturer foundLecturer = lecturerService.findByAccountId(account.getId());
                    if (null != foundLecturer) {
                        // ADD COURSE
                        Course result = courseService.addCourse(course);
                        // Xử lý sau khi add course
                        // check lecturer exist
                        if (result != null) {
                            // Add log lecturer_course
                            // create new lecturerCourseId
                            LecturerCourseId lecturerCourseId = new LecturerCourseId();
                            lecturerCourseId.setLecturerId(foundLecturer.getId());
                            // set course Id that added
                            lecturerCourseId.setCourseId(result.getId());
                            lecturerCourseId.setCreatedDate(LocalDate.now());

                            // save new lecturer manage to the course
                            LecturerCourse lecturerCourse = new LecturerCourse();
                            lecturerCourse.setId(lecturerCourseId);

                            LecturerCourse addLecturerCourseResult = lecturerCourseService.add(lecturerCourse);
                            // check save lecturerCourse to database
                            if (null != addLecturerCourseResult) {

                                // add lecturer to  course
                                List<LecturerCourseId> lecturerCourseIds = new ArrayList<>();
                                lecturerCourseIds.add(addLecturerCourseResult.getId());

                                result.setLecturerCourseIds(lecturerCourseIds);
                                result.setLecturer(foundLecturer);
                                // update course lecturers
                                result = courseService.updateCourse(result);
                                foundLecturer.getCourses().add(course);
                                librarian.getCreatedCourses().add(course);
                                // Thêm course vào danh sách tạo của librarian
                                librarianService.updateLibrarian(librarian);
                                // Thêm course vào danh sách quản lý của lecturer
                                lecturerService.updateLecturer(foundLecturer);

                                if (null != result) {
                                    return "redirect:/librarian/courses/add?success";
                                }
                            }
                        }
                    }
                } else {
                    return "redirect:/librarian/courses/add?error";
                }

            } else {
                Course addedCourse = courseService.addCourse(course);

                librarian.getCreatedCourses().add(course);
                librarianService.updateLibrarian(librarian);
                if (null != addedCourse) {
                    return "redirect:/librarian/courses/add?success";
                }
            }
        }
        return "redirect:/librarian/courses/add?error";

    }

    /**
     * @param courseId
     * @param model
     * @return
     */
    @GetMapping({"/courses/{courseId}/update"})
    public String updateCourseProcess(@PathVariable String courseId,
                                      final Model model) {
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
            List<TrainingType> trainingTypes = trainingTypeService.findAll();
            model.addAttribute("trainingTypes", trainingTypes);
            model.addAttribute("statuses", CourseEnum.Status.values());
            return "librarian/course/librarian_update-course";
        }
    }

    /**
     * @param course
     * @param model
     * @return
     */
    @PostMapping("/courses/update")
    public String updateCourse(@ModelAttribute Course course, final Model model) {
        Course checkExist = courseService.findByCourseId(course.getId());
        if (null == checkExist) {
            return "redirect:/librarian/courses/" + course.getId() + "/update?error";
        } else {
            Course checkCodeDuplicate = courseService.findByCourseCode(course.getCourseCode());
            if (checkCodeDuplicate != null &&
                    !checkExist.getCourseCode().equalsIgnoreCase(course.getCourseCode())) {
                return "redirect:/librarian/courses/" + course.getId() + "/update?error";
            }
            courseService.updateCourse(course);
            return "redirect:/librarian/courses/" + course.getId() + "/update?success";
        }
    }

    /**
     * @return
     */
    @GetMapping({"/courses/list"})
    public String showCourses() {
        return "librarian/course/librarian_courses";
    }

    @GetMapping("/courses/list/{pageIndex}")
    String showCoursesByPage(@PathVariable Integer pageIndex,
                             @RequestParam(required = false, defaultValue = "") String search,
                             final Model model, HttpServletRequest request) {
        Page<Course> page;
        page = courseService.findByCodeOrNameOrDescription(search, search, search, pageIndex, pageSize);
        List<Integer> pages = CommonUtils.pagingFormat(page.getTotalPages(), pageIndex);
        model.addAttribute("pages", pages);
        model.addAttribute("totalPage", page.getTotalPages());
        model.addAttribute("courses", page.getContent());
        model.addAttribute("search", search);
        model.addAttribute("currentPage", pageIndex);
        return "librarian/course/librarian_courses";
    }

    /**
     * @param courseId
     * @param model
     * @return
     */
    @GetMapping({"/courses/{courseId}"})
    public String showCourseDetail(@PathVariable String courseId, final Model model) {
        Course course = courseService.findByCourseId(courseId);
        List<Account> accounts = accountService.findAllLecturer();
        boolean checkLecturerCourse = course.getLecturer().getId() != null;

        model.addAttribute("course", course);
        model.addAttribute("checkLecturerCourse", checkLecturerCourse);
        model.addAttribute("accounts", accounts);
        return "librarian/course/librarian_course-detail";
    }

    /**
     * @param courseId
     * @return
     */
    @GetMapping("/courses/{courseId}/delete")
    public String deleteCourse(@PathVariable String courseId) {
        Course checkExistCourse = courseService.findByCourseId(courseId);
        if (null != checkExistCourse) {
            courseService.softDelete(checkExistCourse);
            return "redirect:/librarian/courses/list/1?success";
        }
        return "redirect:/librarian/courses/list/1?error";
    }

    /**
     * @param courseId
     * @param model
     * @return
     */
    @GetMapping({"/courses/{courseId}/addLecturers"})
    public String addLecturersProcess(@PathVariable String courseId, final Model model) {
        Course course = courseService.findByCourseId(courseId);
        Lecturer lecturers = lecturerService.findByCourseId(courseId);
        List<Account> accounts = accountService.findAllLecturer();
        model.addAttribute("course", course);
        model.addAttribute("lecturers", lecturers);
        model.addAttribute("accounts", accounts);
        return "librarian/course/librarian_add-lecturer-to-course";
    }

    /**
     * @param courseId
     * @param search
     * @param model
     * @return
     */
    @GetMapping({"/courses/{courseId}/updateLecturers"})
    public String updateLecturersProcess(@PathVariable String courseId, @RequestParam String search, final Model model) {
        Lecturer courseLecturers = lecturerService.findByCourseId(courseId);
        List<Account> lecturers = accountService.searchLecturer(search);
        model.addAttribute("courseLecturers", courseLecturers);
        model.addAttribute("lecturers", lecturers);
        return "librarian/course/librarian_add-lecturer-to-course";
    }


//    LECTURER MANAGEMENT

    @GetMapping({"/courses/{courseId}/add-lecture"})
    public String addLecturer(@PathVariable String courseId, final Model model) {
        Course course = courseService.findByCourseId(courseId);
        List<Account> accounts = accountService.findAllLecturer();
        model.addAttribute("course", course);
        model.addAttribute("accounts", accounts);
        return "librarian/course/librarian_course-detail";
    }

    @GetMapping({"/courses/{courseId}/remove-lecture"})
    public String removeLecture(@PathVariable String courseId, final Model model) {
        Course course = courseService.findByCourseId(courseId);
        boolean removed = lecturerService.removeCourse(course.getLecturer().getId(), course);
        boolean removed1 = courseService.removeLecture(courseId);
        if (removed1 && removed) {
            return "redirect:/librarian/courses/{courseId}/add-lecture?success";
        } else return "redirect:/librarian/courses/{courseId}/add-lecture?error";
    }

    /**
     * @param courseId course id
     * @return
     */

    @PostMapping({"/courses/{courseId}/add-lecture"})
    public String addLecturer(@PathVariable String courseId,
                              @RequestParam String lecturerEmail) {
        Account account = accountService.findByEmail(lecturerEmail);

        Lecturer savedLecturer = null;
        if (account == null) {
            Account a = new Account();
            a.setEmail(lecturerEmail);
            Account account1 = accountService.saveAccount(a);
            // save account
            Lecturer lecturer = new Lecturer();
            lecturer.setAccount(account1);
            savedLecturer = lecturerService.addLecturer(lecturer);
            // save lecturer
        } else {
            savedLecturer = lecturerService.findByAccountId(account.getId());
            if (savedLecturer == null) {
                Lecturer lecturer = new Lecturer();
                lecturer.setAccount(account);
                savedLecturer = lecturerService.addLecturer(lecturer);
            }
        }
        Course course = courseService.updateLectureId(courseId, savedLecturer);
        if (course == null) {
            return "redirect:/courses/" + courseId + "/add-lecture?error";
        } else {
            lecturerService.addCourseToLecturer(savedLecturer.getId(), new ObjectId(courseId));
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("maihoa362001@gmail.com");
            message.setTo("ngaydoidemcho93@gmail.com"); // Change to lecturer mail
            String subject = "Notification: Course Management Assignment";
            message.setSubject(subject);

            // Compose the body of the email
            String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
            String body = String.format(
                    "Kính gửi Giảng viên,\n\n" +
                            "Chúng tôi xin thông báo bạn đã được phân công quản lý khóa học '%s' từ thời điểm %s. " +
                            "Đây là một cơ hội tuyệt vời để bạn thể hiện khả năng và đóng góp vào sự phát triển của khóa học.\n\n" +
                            "Vui lòng đăng nhập vào hệ thống quản lý khóa học để cập nhật nội dung, tài liệu và thông tin liên quan đến khóa học. " +
                            "Nếu bạn có bất kỳ thắc mắc hoặc cần sự hỗ trợ, đừng ngần ngại liên hệ với chúng tôi.\n\n" +
                            "Chúc bạn một ngày làm việc hiệu quả và nhiều niềm vui!\n\n" +
                            "Trân trọng,\n Thư viện FPT",
                    course.getCourseName(), dateTime);
            message.setText(body);
            javaMailSender.send(message);
            return "redirect:/librarian/courses/" + courseId + "?success";
        }

    }

    @GetMapping({"/lectures"})
    public String showLectures(final Model model) {
        return "librarian/lecture/librarian_lectures";
    }

    @GetMapping({"/lectures/list"})
    public String showLecture(final Model model) {
        List<Lecturer> lecturers = lecturerService.findAll();
        model.addAttribute("lecturers", lecturers);
        return "librarian/lecture/librarian_lectures";
    }

    @GetMapping("/lectures/list/{pageIndex}")
    String showLectureByPage(@PathVariable Integer pageIndex,
                             @RequestParam(required = false, defaultValue = "") String search,
                             final Model model, HttpServletRequest request) {
//        Page<Course> page;
//        page = courseService.findByCodeOrNameOrDescription(search, search, search, pageIndex, pageSize);
//        List<Integer> pages = CommonUtils.pagingFormat(page.getTotalPages(), pageIndex);
//        model.addAttribute("pages", pages);
//        model.addAttribute("totalPage", page.getTotalPages());
//        model.addAttribute("courses", page.getContent());
//        model.addAttribute("search", search);
//        model.addAttribute("currentPage", pageIndex);
        return "librarian/lecture/librarian_lectures";
    }


    @GetMapping({"/lectures/{lectureId}"})
    public String showLectureDetail(@PathVariable String lectureId, final Model model) {
        Lecturer lecturer = lecturerService.findLecturerById(lectureId);
        model.addAttribute("lecturer", lecturer);
        model.addAttribute("roles", AccountEnum.Role.values());
        model.addAttribute("campuses", AccountEnum.Campus.values());
        model.addAttribute("genders", AccountEnum.Gender.values());
        return "librarian/lecture/librarian_lecture-detail";
    }

    @GetMapping("/lectures/create-lecture")
    public String showAddLectureForm(Model model) {
        List<Course> allCourses = courseService.findAll();
        List<TrainingType> trainingTypes = trainingTypeService.findAll();
        model.addAttribute("allCourses", allCourses);
        model.addAttribute("trainingTypes", trainingTypes);
        model.addAttribute("lecture", new Lecturer());
        return "librarian/lecture/librarian_add-lecture";
    }


    @PostMapping("/lectures/create-lecture")
    public String addLectureCourse(@ModelAttribute Lecturer lecturer, @RequestParam String email) {
        Account lecture = accountService.findByEmail(email);
        if (lecture != null) {
            lecturer.setAccount(lecture);
            if (lecturerService.findLecturerByAccount(lecture) == null) {
                lecturerService.addLecturer(lecturer);
//        lecturer.getAccount().setRole();
                for (int i = 0; i < lecturer.getCourses().size(); i++) {
                    if (lecturer.getCourses().get(i).getLecturer() == null)
                        courseService.updateLectureId(String.valueOf(lecturer.getCourses().get(i)), lecturer);
                }
                return "redirect:/librarian/lectures/create-lecture?success";
            } else
                return "redirect:/librarian/lectures/create-lecture?error";
        } else
            return "redirect:/librarian/lectures/create-lecture?error";

    }

    @PostMapping("/lectures/update")
    public String updateLecture(@ModelAttribute("lecturer") Lecturer updatedLecture, final Model model) {
        // Check if the lectureId is valid (you can add additional validation)
        if (updatedLecture == null) {
            return "redirect:/error"; // Redirect to an error page or handle invalid ID
        }
        // Call the service to update the lecture
        boolean updateSuccess = lecturerService.update(updatedLecture);

        if (updateSuccess) {
            return "redirect:/lectures/" + updatedLecture.getId() + "/success";
        } else {
            return "redirect:/lectures/error"; // Redirect to an error page if the update fails
        }
    }

    //    @GetMapping({"/lectures/{courseId}"})
//    public String showCourseOfLecturer(@PathVariable String courseId, final Model model) {
//        Course course = courseService.findByCourseId(courseId);
//        List<Account> accounts = accountService.findAllLecturer();
//        boolean checkLecturerCourse = course.getLecturer().getId() != null;
//
//        model.addAttribute("course", course);
//        model.addAttribute("checkLecturerCourse", checkLecturerCourse);
//        model.addAttribute("accounts", accounts);
//        return "librarian/lecture/librarian_lecture-course-detail";
//    }
    @GetMapping("/lectures/delete/{id}")
    public String softDeleteLecturer(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        Optional<Lecturer> lecturer = Optional.ofNullable(lecturerService.findLecturerById(id));
        if (lecturer.isPresent()) {
            boolean isDeleted = lecturerService.softDelete(lecturer.get());
            if (isDeleted) {
                redirectAttributes.addFlashAttribute("success", "Lecturer was successfully deleted.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to delete lecturer.");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Lecturer not found.");
        }
        return "redirect:/admin/lectures/list";
    }

    @PostMapping("/lectures/delete/{id}")
    public String deleteLecturer(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        try {
            lecturerService.softDelete(lecturerService.findLecturerById(id));
            redirectAttributes.addFlashAttribute("success", "Lecturer deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting lecturer: " + e.getMessage());
        }
        return "redirect:/admin/lecturers/list";
    }


}
