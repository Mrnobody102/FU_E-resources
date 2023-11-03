package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.model.DocumentNote;
import fpt.edu.eresourcessystem.repository.DocumentNoteRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("studentNoteService")
public class DocumentNoteServiceImpl implements DocumentNoteService {
    private final DocumentNoteRepository documentNoteRepository;

    public DocumentNoteServiceImpl(DocumentNoteRepository documentNoteRepository) {
        this.documentNoteRepository = documentNoteRepository;
    }

    @Override
    public DocumentNote findById(String studentNoteId) {
        Optional<DocumentNote> studentNote = documentNoteRepository.findById(studentNoteId);
        return  studentNote.orElse(null);
    }

    @Override
    public DocumentNote findByDocIdAndStudentId(String docId, String studentId) {
        DocumentNote documentNote = documentNoteRepository.findByDocIdAndStudentId(docId,studentId);
        return documentNote;
    }

    @Override
    public DocumentNote addStudentNote(DocumentNote documentNote) {
        if(null!= documentNote && null== documentNote.getId()){
            if(null!= documentNoteRepository.findByDocIdAndStudentId(documentNote.getDocId(), documentNote.getStudentId())){
                return null;
            }else {
                documentNote.setDeleteFlg(CommonEnum.DeleteFlg.PRESERVED);
                DocumentNote result = documentNoteRepository.save(documentNote);
                return result;
            }
        }
        return null;
    }

    @Override
    public DocumentNote updateStudentNote(DocumentNote documentNote) {
        Optional<DocumentNote> savedStudentNote = documentNoteRepository.findById(documentNote.getId());
        if(savedStudentNote.isPresent()){
            DocumentNote result =  documentNoteRepository.save(documentNote);
            return result;
        }
        return null;
    }

    @Override
    public boolean deleteStudentNote(DocumentNote documentNote) {
        Optional<DocumentNote> check = documentNoteRepository.findById(documentNote.getId());
        if(check.isPresent()){
            // SOFT DELETE
            documentNote.setDeleteFlg(CommonEnum.DeleteFlg.DELETED);
            documentNoteRepository.save(documentNote);
            return true;
        }
        return false;
    }
}
