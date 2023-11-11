package fpt.edu.eresourcessystem.controller.restcontrollers;


import fpt.edu.eresourcessystem.dto.DocumentNoteDTO;
import fpt.edu.eresourcessystem.model.DocumentNote;
import fpt.edu.eresourcessystem.model.Student;
import fpt.edu.eresourcessystem.service.DocumentNoteService;
import fpt.edu.eresourcessystem.service.DocumentService;
import fpt.edu.eresourcessystem.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
public class StudentRestController {
    private final StudentService studentService;
    private final DocumentService documentService;
    private final DocumentNoteService documentNoteService;
    public StudentRestController(StudentService studentService, DocumentService documentService, DocumentNoteService documentNoteService) {
        this.studentService = studentService;
        this.documentService = documentService;
        this.documentNoteService = documentNoteService;
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

    @PostMapping("/my_note/document_notes/add")
    @Transactional
    public DocumentNoteDTO addMyNote(@ModelAttribute DocumentNoteDTO documentNoteDTO){
        Student student = getLoggedInStudent();
        if(null == student){
            return null;
        }else if(null==documentNoteDTO){
            return null;
        }
        documentNoteDTO.setStudentId(student.getId());
        DocumentNote documentNote = documentNoteService.addDocumentNote(new DocumentNote(documentNoteDTO));
        if(null!= documentNote){
            return documentNoteDTO;
        }else {
            return null;
        }
    }
}
