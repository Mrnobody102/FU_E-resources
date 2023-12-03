package fpt.edu.eresourcessystem.repository;

import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.model.StudentNote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository("studentNoteRepository")
public interface StudentNoteRepository extends MongoRepository<StudentNote, String> {
    StudentNote findByIdAndDeleteFlg(String studentNoteId, CommonEnum.DeleteFlg status);
}
