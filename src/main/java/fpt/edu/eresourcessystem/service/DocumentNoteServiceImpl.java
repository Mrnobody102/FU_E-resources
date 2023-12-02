package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.model.DocumentNote;
import fpt.edu.eresourcessystem.repository.DocumentNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("documentNoteService")
@RequiredArgsConstructor
public class DocumentNoteServiceImpl implements DocumentNoteService {
    private final DocumentNoteRepository documentNoteRepository;
    private final MongoTemplate mongoTemplate;
    @Override
    public DocumentNote findById(String studentNoteId) {
        DocumentNote studentNote = documentNoteRepository.findByIdAndDeleteFlg(studentNoteId, CommonEnum.DeleteFlg.PRESERVED);
        return  studentNote;
    }

    @Override
    public DocumentNote findByDocIdAndStudentId(String docId, String studentId) {
        DocumentNote documentNote = documentNoteRepository
                .findByDocIdAndStudentIdAndDeleteFlg(docId,studentId, CommonEnum.DeleteFlg.PRESERVED);
        return documentNote;
    }

    @Override
    public DocumentNote addDocumentNote(DocumentNote documentNote) {
        if(null!= documentNote && null== documentNote.getId()){
            if(null!= documentNoteRepository
                    .findByDocIdAndStudentIdAndDeleteFlg(documentNote.getDocId(), documentNote.getStudentId(),
                            CommonEnum.DeleteFlg.PRESERVED)){
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
        DocumentNote check = documentNoteRepository.findByIdAndDeleteFlg(documentNote.getId(), CommonEnum.DeleteFlg.PRESERVED);
        if(null != check){
            // SOFT DELETE
            documentNote.setDeleteFlg(CommonEnum.DeleteFlg.DELETED);
            documentNoteRepository.save(documentNote);
            return true;
        }
        return false;
    }

    @Override
    public List<DocumentNote> findByStudent(String studentId) {
        Criteria criteria = Criteria.where("studentId").is(studentId).and("deleteFlg").is(CommonEnum.DeleteFlg.PRESERVED);
        Query query = new Query(criteria).with(Sort.by(Sort.Direction.DESC, "lastModifiedDate"));
        return mongoTemplate.find(query, DocumentNote.class);
    }
}
