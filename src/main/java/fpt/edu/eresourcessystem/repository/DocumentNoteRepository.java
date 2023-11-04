package fpt.edu.eresourcessystem.repository;

import fpt.edu.eresourcessystem.model.DocumentNote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("documentNoteRepository")
public interface DocumentNoteRepository extends MongoRepository<DocumentNote, String> {

    Optional<DocumentNote> findById(String studentNoteId);

    DocumentNote findByDocIdAndStudentId(String docId, String studentId);
}
