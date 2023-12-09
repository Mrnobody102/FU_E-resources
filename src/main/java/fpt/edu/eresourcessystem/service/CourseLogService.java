package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.dto.Response.CourseLogResponseDto;
import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.CourseLog;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CourseLogService {
    CourseLog addCourseLog(CourseLog courseLog);
    List<CourseLogResponseDto> findAllSortedByCreatedDate();
    void deleteCourseLog(CourseLog courseLog);
    List<String> findByLecturer(String email);
    List<CourseLog> findByCourseCodeOrCodeName(String search);
}
