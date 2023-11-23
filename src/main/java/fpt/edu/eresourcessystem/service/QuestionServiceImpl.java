package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.model.*;
import fpt.edu.eresourcessystem.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("questionService")
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final DocumentService documentService; // just for lecturer question, need to be change soon
    private final  StudentService studentService;
    private final AnswerService answerService;

    public QuestionServiceImpl(QuestionRepository questionRepository, DocumentService documentService, StudentService studentService, AnswerService answerService) {
        this.questionRepository = questionRepository;
        this.documentService = documentService;
        this.studentService = studentService;
        this.answerService = answerService;
    }

    @Override
    public List<Question> findByDocId(Document document) {
        List<Question> questions = questionRepository.findByDocumentId(document);
        return questions;
    }

    @Override
    public List<Question> findByDocIdAndStudentId(Document document, Student student) {
        List<Question> questions = questionRepository.findByDocumentIdAndStudent(document, student);
        return questions;
    }

    @Override
    public List<Question> findByStudent(Student student) {
        List<Question> questions = questionRepository.findByStudent(student);
        return questions;
    }

    @Override
    public List<Question> findByLecturer(Lecturer lecturer) {
        List<Document> documents = documentService.findByLecturer(lecturer);
        List<Question> questions = new ArrayList<>();
        for(Document document : documents) {
            List<Question> questionDocs = questionRepository.findByDocumentId(document);
            for(Question question:questionDocs)
            questions.add(question);
        }
        return questions;
    }

    @Override
    public Question findById(String quesId) {
        Optional<Question> question = questionRepository.findById(quesId);
        return question.orElse(null);
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
        Optional<Question> savedQuestion = questionRepository.findById(question.getId());
        if (savedQuestion.isPresent()) {
            Question result = questionRepository.save(question);
            return result;
        }
        return null;
    }

    @Override
    public boolean deleteQuestion(Question question) {
        Optional<Question> check = questionRepository.findById(question.getId());
        if (check.isPresent()) {
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
