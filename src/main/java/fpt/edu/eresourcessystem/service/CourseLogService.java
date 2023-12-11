package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.dto.Response.CourseLogResponseDto;
import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.CourseLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface CourseLogService {
    CourseLog addCourseLog(CourseLog courseLog);

    List<CourseLogResponseDto> findAllSortedByCreatedDate();

    void deleteCourseLog(CourseLog courseLog);

    List<String> findByLecturer(String email);

    List<CourseLog> findByCourseCodeOrCodeName(String search);

    Page<CourseLog> getLogsBySearchAndDate(String search
            , LocalDate startDate, LocalDate endDate, int pageIndex, int pageSize);
    List<CourseLog> getLogsBySearchAndDateListAll(String search
            , LocalDate startDate, LocalDate endDate);
}
