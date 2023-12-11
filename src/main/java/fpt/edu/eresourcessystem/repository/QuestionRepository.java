package fpt.edu.eresourcessystem.repository;

import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.enums.QuestionAnswerEnum;
import fpt.edu.eresourcessystem.model.Document;
import fpt.edu.eresourcessystem.model.Lecturer;
import fpt.edu.eresourcessystem.model.Question;
import fpt.edu.eresourcessystem.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("questionRepository")
public interface QuestionRepository extends MongoRepository<Question, String> {
    List<Question> findAll();
    Question findByIdAndDeleteFlg(String id, CommonEnum.DeleteFlg status);
    List<Question> findByDocumentIdAndDeleteFlg(Document document, CommonEnum.DeleteFlg status);
    List<Question> findByDocumentIdAndStudentAndDeleteFlg(Document document, Student student, CommonEnum.DeleteFlg status);
    List<Question> findByStudentAndDeleteFlg(Student student, CommonEnum.DeleteFlg status);
    List<Question> findByLecturerAndDeleteFlg(String lecturer, CommonEnum.DeleteFlg status);

}
