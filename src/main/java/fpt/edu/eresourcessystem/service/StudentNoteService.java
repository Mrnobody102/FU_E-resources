package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.StudentNote;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StudentNoteService {
    Page<StudentNote> getNoteByStudent(String studentId, int pageIndex, int pageSize);
}
