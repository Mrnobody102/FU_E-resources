package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.dto.Response.QuestionResponseDto;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.enums.QuestionAnswerEnum;
import fpt.edu.eresourcessystem.model.*;
import fpt.edu.eresourcessystem.repository.QuestionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static fpt.edu.eresourcessystem.enums.CommonEnum.DeleteFlg.PRESERVED;

@AllArgsConstructor
@Service("questionService")
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final DocumentService documentService; // just for lecturer question, need to be change soon
    private final StudentService studentService;
    private final AnswerService answerService;
    private final MongoTemplate mongoTemplate;
    private final SimpMessagingTemplate messagingTemplate;


    @Override
    public List<Question> findByDocId(Document document) {
        List<Question> questions = questionRepository.findByDocumentIdAndDeleteFlg(document, PRESERVED);
        return questions;
    }

    @Override
    public List<Question> findByDocIdAndStudentId(Document document, Student student) {
        List<Question> questions = questionRepository.findByDocumentIdAndStudentAndDeleteFlg(document, student, PRESERVED);
        return questions;
    }

    @Override
    public List<QuestionResponseDto> findWaitReplyQuestionForStudent(String studentId) {
        Query query = new Query(Criteria.where("student.id").is(studentId)
                .and("status").is(QuestionAnswerEnum.Status.CREATED));
        List<Question> questions = mongoTemplate.find(query, Question.class);
        List<QuestionResponseDto> responseList = questions.stream()
                .filter(entity -> PRESERVED.equals(entity.getDeleteFlg()))
                .map(entity -> new QuestionResponseDto(entity))
                .collect(Collectors.toList());
        return responseList;
    }

    @Override
    public List<QuestionResponseDto> findNewQuestionForLecturer(String lecturerEmail) {
        Query query = new Query(Criteria.where("lecturer").is(lecturerEmail)
                .and("status").is(QuestionAnswerEnum.Status.CREATED))
                .limit(5).skip(0);
        List<Question> questions = mongoTemplate.find(query, Question.class);
        List<QuestionResponseDto> responseList = questions.stream()
                .filter(entity -> PRESERVED.equals(entity.getDeleteFlg()))
                .map(entity -> new QuestionResponseDto(entity))
                .collect(Collectors.toList());
        return responseList;
    }

    @Override
    public List<QuestionResponseDto> findNewReplyQuestionStudent(String studentId) {
        Query query = new Query(Criteria.where("student.id").is(studentId)
                .and("status").is(QuestionAnswerEnum.Status.REPLIED))
                .limit(5).skip(0);
        List<Question> questions = mongoTemplate.find(query, Question.class);
        List<QuestionResponseDto> responseList = questions.stream()
                .filter(entity -> PRESERVED.equals(entity.getDeleteFlg()))
                .map(entity -> new QuestionResponseDto(entity))
                .collect(Collectors.toList());
        return responseList;
    }

    @Override
    public List<QuestionResponseDto> findRepliedQuestionForLecturer(String lecturerEmail) {
        QuestionAnswerEnum.Status [] statuses = {QuestionAnswerEnum.Status.REPLIED,QuestionAnswerEnum.Status.READ, QuestionAnswerEnum.Status.UNREAD};
        Query query = new Query(Criteria.where("lecturer").is(lecturerEmail)
                .and("status").in(statuses))
                .limit(5).skip(0);
        List<Question> questions = mongoTemplate.find(query, Question.class);
        List<QuestionResponseDto> responseList = questions.stream()
                .filter(entity -> PRESERVED.equals(entity.getDeleteFlg()))
                .map(entity -> new QuestionResponseDto(entity))
                .collect(Collectors.toList());
        return responseList;
    }

    @Override
    public List<Question> findByStudent(Student student) {
        List<Question> questions = questionRepository.findByStudentAndDeleteFlg(student, PRESERVED);
        return questions;
    }

    @Override
    public List<Question> findByLecturerMail(String lecturerMail) {
        Query query = new Query(Criteria.where("lecturer").is(lecturerMail)
                .and("deleteFlg").is(PRESERVED))
                .limit(5).skip(0);
        List<Question> questions = mongoTemplate.find(query, Question.class);
        return questions;
    }

    @Override
    public Question findById(String quesId) {
        Question question = questionRepository.findByIdAndDeleteFlg(quesId, PRESERVED);
        return question;
    }

    @Override
    public Question addQuestion(Question question) {
        if (null != question) {
            if (null == question.getId()) {
                Question result = questionRepository.save(question);
                return result;
            } else if (!questionRepository.findById(question.getId().trim()).isPresent()) {
                Question result = questionRepository.save(question);
                return result;
            }
        }
        return null;
    }

    @Override
    public Question updateQuestion(Question question) {
        Question savedQuestion = questionRepository.findByIdAndDeleteFlg(question.getId(), PRESERVED);
        if (null!= savedQuestion) {
            Question result = questionRepository.save(question);
            return result;
        }
        return null;
    }

    @Override
    public boolean deleteQuestion(Question question) {
        Question check = questionRepository.findByIdAndDeleteFlg(question.getId(), PRESERVED);
        if (null != check) {
            // Soft delete topic first
            for (Answer answer : question.getAnswers()) {
                answerService.deleteAnswer(answer);
            }
            // SOFT DELETE Course
            question.setDeleteFlg(CommonEnum.DeleteFlg.DELETED);
            questionRepository.save(question);
            return true;
        }
        return false;
    }
}
