package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.dto.StudentNoteDTO;
import fpt.edu.eresourcessystem.model.StudentNote;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StudentNoteService {
    Page<StudentNote> getNoteByStudent(String studentId, int pageIndex, int pageSize);
    String addFile(MultipartFile file) throws IOException;

    StudentNote addStudentNote(StudentNoteDTO studentNoteDTO);

}
