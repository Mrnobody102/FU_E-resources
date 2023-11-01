package fpt.edu.eresourcessystem.controller.restcontrollers;

import fpt.edu.eresourcessystem.dto.StudentNoteDTO;
import fpt.edu.eresourcessystem.model.Student;
import fpt.edu.eresourcessystem.model.StudentNote;
import fpt.edu.eresourcessystem.service.StudentNoteService;
import fpt.edu.eresourcessystem.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student/notes")
public class StudentNoteRestController {
    private final StudentNoteService studentNoteService;
    private final StudentService studentService;

    public StudentNoteRestController(StudentNoteService studentNoteService, StudentService studentService) {
        this.studentNoteService = studentNoteService;
        this.studentService = studentService;
    }
    public Student getLoggedInStudent() {
        return studentService.findAll().get(0);
    }


    @PostMapping("/add/{docId}")
    public ResponseEntity<StudentNote> addStudentNote(@RequestParam String content) {
        // get current account
        Student student = getLoggedInStudent();
        StudentNoteDTO studentNoteDTO = new StudentNoteDTO();
        studentNoteDTO.setContent(content);
        StudentNote result = studentNoteService.addStudentNote(new StudentNote(studentNoteDTO));
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{noteId}/update")
    public ResponseEntity<StudentNote> updateStudentNote(@PathVariable(name = "noteId", required = false) String noteId, @RequestBody StudentNote studentNote){
        StudentNote result = studentNoteService.updateStudentNote(studentNote);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{noteId}/delete")
    public ResponseEntity<StudentNote> delete(@PathVariable(name = "noteId", required = false) String noteId) {
        if(null!= noteId){
            StudentNote studentNote = studentNoteService.findById(noteId);
            boolean delete = studentNoteService.deleteStudentNote(studentNote);
            if(delete){
                return ResponseEntity.ok(studentNote);
            }
            return null;
        }
        return null;

    }
    @GetMapping("/{noteId}")
    public StudentNote getNoteDetail(@PathVariable(name = "noteId", required = false) String noteId) {
        StudentNote studentNote = studentNoteService.findById(noteId);
        return studentNote;
    }
}
