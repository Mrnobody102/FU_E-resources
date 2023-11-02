package fpt.edu.eresourcessystem.repository;

import fpt.edu.eresourcessystem.model.StudentNote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("studentNoteRepository")
public interface StudentNoteRepository extends MongoRepository<StudentNote, String> {

    Optional<StudentNote> findById(String studentNoteId);

    StudentNote findByDocIdAndStudentId(String docId, String studentId);
}
