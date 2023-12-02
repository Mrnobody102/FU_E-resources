package fpt.edu.eresourcessystem.repository;

import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.model.DocumentNote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("documentNoteRepository")
public interface DocumentNoteRepository extends MongoRepository<DocumentNote, String> {

    Optional<DocumentNote> findById(String studentNoteId);
    DocumentNote findByIdAndDeleteFlg(String studentNoteId, CommonEnum.DeleteFlg status );

    DocumentNote findByDocIdAndStudentIdAndDeleteFlg(String docId, String studentId, CommonEnum.DeleteFlg status);

    List<DocumentNote> findByStudentId(String studentId);
}
