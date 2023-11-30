package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.dto.DocumentNoteDto;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.model.DocumentNote;
import fpt.edu.eresourcessystem.model.Notification;
import fpt.edu.eresourcessystem.repository.DocumentNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("documentNoteService")
@RequiredArgsConstructor
public class DocumentNoteServiceImpl implements DocumentNoteService {
    private final DocumentNoteRepository documentNoteRepository;
    private final MongoTemplate mongoTemplate;
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
    public DocumentNote addDocumentNote(DocumentNote documentNote) {
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
    public DocumentNote updateDocumentNote(DocumentNote documentNote) {
        if (null == documentNote || null==documentNote.getId()){
            return null;
        }
        Optional<DocumentNote> savedStudentNote = documentNoteRepository.findById(documentNote.getId());
        if(savedStudentNote.isPresent()){
            DocumentNote result =  documentNoteRepository.save(documentNote);
            return result;
        }
        return null;
    }

    @Override
    public boolean deleteDocumentNote(DocumentNote documentNote) {
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
