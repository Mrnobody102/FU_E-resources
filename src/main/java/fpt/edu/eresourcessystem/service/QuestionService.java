package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.dto.Response.QuestionResponseDto;
import fpt.edu.eresourcessystem.enums.QuestionAnswerEnum;
import fpt.edu.eresourcessystem.model.Document;
import fpt.edu.eresourcessystem.model.Lecturer;
import fpt.edu.eresourcessystem.model.Question;
import fpt.edu.eresourcessystem.model.Student;
import org.springframework.data.domain.Page;

import java.util.List;

public interface QuestionService {

    List<Question> findByDocId(Document document);
    List<QuestionResponseDto> findByStudentLimitAndSkip(Student student, Document document, int limit, int skip);

    List<QuestionResponseDto> findByDocumentLimitAndSkip(Document document, int limit, int skip);

    List<QuestionResponseDto> findByOtherStudentLimitAndSkip(Student student, Document document, int limit, int skip);
    List<Question> findByDocIdAndStudentId(Document document, Student student);
    List<QuestionResponseDto> findWaitReplyQuestionForStudent(String studentId);
    List<QuestionResponseDto> findNewQuestionForLecturer(String lecturerEmail);
    List<QuestionResponseDto> findNewReplyQuestionStudent(String studentId);
    List<QuestionResponseDto> findRepliedQuestionForLecturer(String lecturerId);
    List<Question> findByStudent(Student student);

    List<Question> findByLecturerMail(String lecturerMail);

    Question findById(String quesId);
    Question addQuestion(Question question);

    Question updateQuestion(Question question);
    boolean deleteQuestion(Question question);

    Page<Question> findByStudentAndSearch(Student student, String search, QuestionAnswerEnum.Status status, int pageIndex, Integer pageSize);
}
