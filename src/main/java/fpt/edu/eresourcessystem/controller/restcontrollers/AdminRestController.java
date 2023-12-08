package fpt.edu.eresourcessystem.controller.restcontrollers;

import fpt.edu.eresourcessystem.dto.AccountDto;
import fpt.edu.eresourcessystem.dto.AnswerDto;
import fpt.edu.eresourcessystem.dto.Response.AccountResponseDto;
import fpt.edu.eresourcessystem.dto.Response.AnswerResponseDto;
import fpt.edu.eresourcessystem.dto.Response.DataTablesResponse;
import fpt.edu.eresourcessystem.enums.QuestionAnswerEnum;
import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.model.Answer;
import fpt.edu.eresourcessystem.model.Feedback;
import fpt.edu.eresourcessystem.model.Question;
import fpt.edu.eresourcessystem.service.AccountService;
import fpt.edu.eresourcessystem.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminRestController {

    private final AccountService accountService;
    private final FeedbackService feedbackService;

    @GetMapping("/feedbacks")
    public ResponseEntity<DataTablesResponse<Feedback>> getAllFeedbacks(
            @RequestParam(value = "start", defaultValue = "0") Integer start,
            @RequestParam(value = "length", defaultValue = "2") Integer length,
            @RequestParam(value = "draw", defaultValue = "1") Integer draw,
            @RequestParam(value = "minDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date minDate,
            @RequestParam(value = "maxDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date maxDate) {

        Page<Feedback> feedbacksPage = feedbackService.findAllByDateRange(minDate, maxDate, PageRequest.of(start / length, length));
        DataTablesResponse<Feedback> response = new DataTablesResponse<>();

        response.setData(feedbacksPage.getContent());
        response.setDraw(draw);
        response.setRecordsTotal(feedbacksPage.getTotalElements());
        response.setRecordsFiltered(feedbacksPage.getTotalElements());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/account", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    @Transactional
    public ResponseEntity<AccountResponseDto> getAccountByEmail(@ModelAttribute AccountDto accountDto,
                                                    @RequestParam String email) {
        Account account = accountService.findByEmail(email);
        if(null == account){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AccountResponseDto accountResponseDto = new AccountResponseDto(account);
        return new ResponseEntity<>(accountResponseDto, HttpStatus.OK);
    }

    @GetMapping("/feedback/respond")
    public ResponseEntity<?> updateFeedbackStatus(@RequestParam("id") String feedbackId,
                                                  @RequestParam("status") String status) {
        try {
            feedbackService.updateFeedbackStatus(feedbackId, status);
            return ResponseEntity.ok("Feedback status updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating feedback status.");
        }
    }
}
