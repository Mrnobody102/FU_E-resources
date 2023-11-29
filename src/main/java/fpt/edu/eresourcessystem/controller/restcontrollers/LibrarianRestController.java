package fpt.edu.eresourcessystem.controller.restcontrollers;

import fpt.edu.eresourcessystem.dto.Response.DataTablesResponse;
import fpt.edu.eresourcessystem.dto.Response.LecturerDto;
import fpt.edu.eresourcessystem.model.Lecturer;
import fpt.edu.eresourcessystem.service.LecturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/librarian")
public class LibrarianRestController {

    @Autowired
    private LecturerService lecturerService;

    @GetMapping("/lectures/list")
    @ResponseBody
    public ResponseEntity<DataTablesResponse<LecturerDto>> getLecturers(
            @RequestParam("start") int start,
            @RequestParam("length") int length,
            @RequestParam("draw") int draw,
            @RequestParam(value = "search[value]", required = false) String searchValue) {

        // Xử lý yêu cầu từ DataTables và trả về dữ liệu tương ứng
        List<Lecturer> lecturer = lecturerService.findLecturers(start, length, searchValue);
        List<LecturerDto> lecturers = lecturer.stream()
                .map(LecturerDto::new)
                .collect(Collectors.toList());

        int totalLecturers = lecturerService.getTotalLecturers(); // Tổng số hàng trong tập dữ liệu

        DataTablesResponse<LecturerDto> response = new DataTablesResponse<>();
        response.setData(lecturers);
        response.setDraw(draw);
        response.setRecordsTotal(totalLecturers);
        response.setRecordsFiltered(lecturers.size());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
