package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.StudentNote;
import fpt.edu.eresourcessystem.repository.StudentNoteRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("studentNoteService")
public class StudentNoteServiceImpl implements  StudentNoteService{
    private final StudentNoteRepository studentNoteRepository;

    public StudentNoteServiceImpl(StudentNoteRepository studentNoteRepository) {
        this.studentNoteRepository = studentNoteRepository;
    }

    @Override
    public StudentNote findById(String studentNoteId) {
        Optional<StudentNote> studentNote = studentNoteRepository.findById(studentNoteId);
        return  studentNote.orElse(null);
    }

    @Override
    public StudentNote findByDocIdAndStudentId(String docId, String studentId) {
        StudentNote studentNote = studentNoteRepository.findByDocIdAndStudentId(docId,studentId);
        return studentNote;
    }

    @Override
    public StudentNote addStudentNote(StudentNote studentNote) {
        if(null!=studentNote && null==studentNote.getId()){
            if(null!=studentNoteRepository.findByDocIdAndStudentId(studentNote.getDocId(),studentNote.getStudentId())){
                return null;
            }else {
                studentNote.setDeleteFlg(CommonEnum.DeleteFlg.PRESERVED);
                StudentNote result = studentNoteRepository.save(studentNote);
                return result;
            }
        }
        return null;
    }

    @Override
    public StudentNote updateStudentNote(StudentNote studentNote) {
        Optional<StudentNote> savedStudentNote = studentNoteRepository.findById(studentNote.getId());
        if(savedStudentNote.isPresent()){
            StudentNote result =  studentNoteRepository.save(studentNote);
            return result;
        }
        return null;
    }

    @Override
    public boolean deleteStudentNote(StudentNote studentNote) {
        Optional<StudentNote> check = studentNoteRepository.findById(studentNote.getId());
        if(check.isPresent()){
            // SOFT DELETE
            studentNote.setDeleteFlg(CommonEnum.DeleteFlg.DELETED);
            studentNoteRepository.save(studentNote);
            return true;
        }
        return false;
    }
}
