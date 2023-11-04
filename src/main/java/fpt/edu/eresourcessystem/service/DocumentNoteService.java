package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.DocumentNote;

public interface DocumentNoteService {
    DocumentNote findById(String studentNoteId);
    DocumentNote findByDocIdAndStudentId(String docId, String studentId);
    DocumentNote addStudentNote(DocumentNote documentNote);
    DocumentNote updateStudentNote(DocumentNote documentNote);
    boolean deleteStudentNote(DocumentNote documentNote);
}
