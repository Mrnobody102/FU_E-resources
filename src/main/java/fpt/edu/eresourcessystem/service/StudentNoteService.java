package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.StudentNote;

public interface StudentNoteService {
    StudentNote findById(String studentNoteId);
    StudentNote findByDocIdAndStudentId(String docId, String studentId);
    StudentNote addStudentNote(StudentNote studentNote);
    StudentNote updateStudentNote(StudentNote studentNote);
    boolean deleteStudentNote(StudentNote studentNote);
}
