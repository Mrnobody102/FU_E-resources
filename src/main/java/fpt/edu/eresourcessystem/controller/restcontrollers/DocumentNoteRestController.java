package fpt.edu.eresourcessystem.controller.restcontrollers;

import fpt.edu.eresourcessystem.dto.DocumentNoteDTO;
import fpt.edu.eresourcessystem.model.DocumentNote;
import fpt.edu.eresourcessystem.model.Student;
import fpt.edu.eresourcessystem.service.DocumentNoteService;
import fpt.edu.eresourcessystem.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/student/document/note")
public class DocumentNoteRestController {
    private final DocumentNoteService documentNoteService;
    private final StudentService studentService;

    public DocumentNoteRestController(DocumentNoteService documentNoteService, StudentService studentService) {
        this.documentNoteService = documentNoteService;
        this.studentService = studentService;
    }
    public Student getLoggedInStudent() {
        return studentService.findAll().get(0);
    }


    @PostMapping("/add/{docId}")
    public ResponseEntity<DocumentNote> addStudentNote(@RequestParam String content) {
        // get current account
        Student student = getLoggedInStudent();
        DocumentNoteDTO documentNoteDTO = new DocumentNoteDTO();
        documentNoteDTO.setContent(content);
        DocumentNote result = documentNoteService.addDocumentNote(new DocumentNote(documentNoteDTO));
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{noteId}/update")
    public ResponseEntity<DocumentNote> updateStudentNote(@PathVariable(name = "noteId", required = false) String noteId, @RequestBody DocumentNote documentNote){
        DocumentNote result = documentNoteService.updateDocumentNote(documentNote);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{noteId}/delete")
    public ResponseEntity<DocumentNote> delete(@PathVariable(name = "noteId", required = false) String noteId) {
        if(null!= noteId){
            DocumentNote documentNote = documentNoteService.findById(noteId);
            boolean delete = documentNoteService.deleteDocumentNote(documentNote);
            if(delete){
                return ResponseEntity.ok(documentNote);
            }
            return null;
        }
        return null;

    }
    @GetMapping("/{noteId}")
    public DocumentNote getNoteDetail(@PathVariable(name = "noteId", required = false) String noteId) {
        DocumentNote documentNote = documentNoteService.findById(noteId);
        return documentNote;
    }
}
