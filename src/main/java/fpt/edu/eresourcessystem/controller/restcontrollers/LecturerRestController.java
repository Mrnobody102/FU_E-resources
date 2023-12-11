package fpt.edu.eresourcessystem.controller.restcontrollers;


import fpt.edu.eresourcessystem.dto.AnswerDto;
import fpt.edu.eresourcessystem.dto.Response.QuestionResponseDto;
import fpt.edu.eresourcessystem.dto.UserLogDto;
import fpt.edu.eresourcessystem.enums.AccountEnum;
import fpt.edu.eresourcessystem.enums.CourseEnum;
import fpt.edu.eresourcessystem.enums.QuestionAnswerEnum;
import fpt.edu.eresourcessystem.model.*;
import fpt.edu.eresourcessystem.dto.Response.AnswerResponseDto;
import fpt.edu.eresourcessystem.service.*;
import fpt.edu.eresourcessystem.service.s3.ImageService;
import fpt.edu.eresourcessystem.service.s3.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lecturer")
@RequiredArgsConstructor
public class LecturerRestController {
    private final LecturerService lecturerService;
    private final DocumentService documentService;
    private final AnswerService answerService;
    private final QuestionService questionService;
    private final AccountService accountService;
    private final UserLogService userLogService;
    private final ImageService imageService;
    private final StorageService storageService;
    private final CourseLogService courseLogService;

    private UserLog addUserLog(String url) {
        UserLog userLog = new UserLog(new UserLogDto(url, getLoggedInLecturer().getAccount().getEmail(), AccountEnum.Role.LECTURER));
        userLog = userLogService.addUserLog(userLog);
        return userLog;
    }

    private void addCourseLog(String courseId, String courseCode, String courseName,
                              CourseEnum.LecturerAction action,
                              CourseEnum.CourseObject object,
                              String objectId,
                              String objectName,
                              String email,
                              String oldContent,
                              String newContent) {
        CourseLog courseLog = new CourseLog(courseId,courseCode,courseName, action, object, objectId, objectName, email, oldContent, newContent);
        courseLogService.addCourseLog(courseLog);
    }


    public Lecturer getLoggedInLecturer() {
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Account loggedInAccount = accountService.findByEmail(loggedInEmail);
        Lecturer loggedInLecturer = lecturerService.findByAccountId(loggedInAccount.getId());
        return loggedInLecturer;
    }

    @PostMapping(value = "/answer/add", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    @Transactional
    public ResponseEntity<AnswerResponseDto> addQuestion(@ModelAttribute AnswerDto answerDTO,
                                                         @RequestParam String docId,
                                                         @RequestParam String quesId) {
        Lecturer lecturer = getLoggedInLecturer();
        Document document = documentService.findById(docId);
        Question question = questionService.findById(quesId);
        if (null == lecturer || null == answerDTO || null == document || null == question) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        answerDTO.setLecturer(lecturer);
        answerDTO.setQuestionId(question);
        answerDTO.setDocumentId(document);
        Answer answer = answerService.addAnswer(new Answer(answerDTO));
        if (null != answer) {
            // update list answer of the question
            question.getAnswers().add(answer);
            question.setStatus(QuestionAnswerEnum.Status.REPLIED);
            questionService.updateQuestion(question);
            // add log
            addUserLog("/api/lecturer/answers/add/" + answer.getId());

            //add course log
            Course course = document.getTopic().getCourse();
            addCourseLog(course.getId(),
                    course.getCourseCode(),
                    course.getCourseName(),
                    CourseEnum.LecturerAction.UPDATE,
                    CourseEnum.CourseObject.DOCUMENT,
                    document.getId(),
                    document.getTitle(),
                    getLoggedInLecturer().getAccount().getEmail(),
                    null, null);
            AnswerResponseDto answerResponseDTO = new AnswerResponseDto(answer);
            return new ResponseEntity<>(answerResponseDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/answers/get/{questionId}", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<AnswerResponseDto>> getAnswerOfQuestion(@PathVariable String questionId) {
        Question question = questionService.findById(questionId);
        List<Answer> answers = answerService.findByQuestion(question);
        List<AnswerResponseDto> answerResponseDtos = new ArrayList<>();
        if (null != answers) {
            for (Answer answer : answers) {
                answerResponseDtos.add(new AnswerResponseDto(answer));
//                System.out.println(new AnswerResponseDto(answer));
            }
            ResponseEntity<List<AnswerResponseDto>> responseEntity = new ResponseEntity<>(answerResponseDtos, HttpStatus.OK);
            // add log
            addUserLog("/api/lecturer/answers/get/" + questionId);
            return responseEntity;
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/my_question/new_question", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<QuestionResponseDto>> getNewReplyQuestion() {
        Lecturer lecturer = getLoggedInLecturer();
        if (null == lecturer) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            List<QuestionResponseDto> questionResponseDto = questionService.findNewQuestionForLecturer(lecturer.getAccount().getEmail());
            // add log
            addUserLog("/api/lecturer/my_question/new_question");
            ResponseEntity<List<QuestionResponseDto>> responseEntity = new ResponseEntity<>(questionResponseDto, HttpStatus.OK);
            return responseEntity;
        }

    }

    @GetMapping(value = "/my_question/replied_question", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<QuestionResponseDto>> getRepliedQuestion() {
        Lecturer lecturer = getLoggedInLecturer();
        if (null == lecturer) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            List<QuestionResponseDto> questionResponseDto = questionService.findRepliedQuestionForLecturer(lecturer.getAccount().getEmail());
            // add log
            addUserLog("/api/lecturer/my_question/replied_question");
            ResponseEntity<List<QuestionResponseDto>> responseEntity = new ResponseEntity<>(questionResponseDto, HttpStatus.OK);
            return responseEntity;
        }

    }

    @PostMapping(value = "/my_question/replies/{answerId}/update", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    @Transactional
    public ResponseEntity<AnswerResponseDto> updateReply(@PathVariable String answerId, @RequestParam String answerContent) {
        Answer answer = answerService.findById(answerId);
        if (null == answer) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            if (null != answerContent && "" != answerContent.trim()) {
                answer.setAnswer(answerContent);
                answer.setStatus(QuestionAnswerEnum.Status.READ);
                answer = answerService.updateAnswer(answer);
                // add log
                addUserLog("/api/lecturer/my_question/replies/" + answerId + "/update");
                ResponseEntity<AnswerResponseDto> responseEntity = new ResponseEntity<>(new AnswerResponseDto(answer), HttpStatus.OK);
                return responseEntity;
            } else {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }
    }

    @PostMapping(value = "/my_question/replies/{answerId}/delete", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    @Transactional
    public ResponseEntity<AnswerResponseDto> deleteReply(@PathVariable String answerId) {
        Answer answer = answerService.findById(answerId);
        if (null == answer) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            boolean check = answerService.deleteAnswer(answer);
            if (check) {
                // add log
                addUserLog("/api/lecturer/my_question/replies/" + answerId + "/delete");
                //chage list answer
                Question question = questionService.findById(answer.getQuestion().getId());
                question.getAnswers().remove(answer);
                questionService.updateQuestion(question);
                return new ResponseEntity(HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }
    }

    @PostMapping("/upload_image_editor")
    @ResponseBody
    public Map<String, Object> imageUpload(MultipartRequest request) throws IOException {
        Map<String, Object> responseData = new HashMap<>();
        try {
            MultipartFile file = request.getFile("upload");
            String s3Url = imageService.uploadImage(file);
            responseData.put("uploaded", true);
            responseData.put("url", s3Url);
            return responseData;
        } catch (IOException e) {
            responseData.put("uploaded", false);
            return responseData;
        }
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("fileName") String fileName) {
        byte[] content = storageService.downloadFile(fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);
        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }

}
