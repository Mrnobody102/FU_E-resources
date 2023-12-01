package fpt.edu.eresourcessystem.controller.restcontrollers;

import fpt.edu.eresourcessystem.dto.Response.DataTablesResponse;
import fpt.edu.eresourcessystem.model.Feedback;
import fpt.edu.eresourcessystem.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    @Autowired
    private FeedbackService feedbackService;

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


}
