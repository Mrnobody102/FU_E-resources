package fpt.edu.eresourcessystem.controller.restcontrollers;


import fpt.edu.eresourcessystem.model.Student;
import fpt.edu.eresourcessystem.service.DocumentService;
import fpt.edu.eresourcessystem.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student")
public class StudentRestController {
    private final StudentService studentService;
    private final DocumentService documentService;

    public StudentRestController(StudentService studentService, DocumentService documentService) {
        this.studentService = studentService;
        this.documentService = documentService;
    }

    public Student getLoggedInStudent() {
        return studentService.findAll().get(0);
    }
    @GetMapping("/documents/{documentId}/save_document")
    public String saveDocument(@PathVariable String documentId) {
        // get account authorized
        Student student = getLoggedInStudent();
        if (null != documentService.findById(documentId)) {
            boolean result = studentService.saveADoc(student.getId(), documentId);
            if (result) {
                return "saved";
            } else {
                return "unsaved";
            }

        }
        return "exception";
    }

    @GetMapping("/documents/{documentId}/unsaved_document")
    public String unsavedDoc(@PathVariable String documentId,
                             HttpServletRequest request,
                             HttpSession session) {
        // get account authorized
        Student student = getLoggedInStudent();
        if (null != documentService.findById(documentId)) {
            boolean result =  studentService.unsavedADoc(student.getId(), documentId);
            if (result) {
                return "unsaved";
            } else {
                return "saved";
            }
        }
        return "exception";
    }
}
