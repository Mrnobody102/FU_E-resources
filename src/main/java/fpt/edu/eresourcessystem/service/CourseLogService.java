package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.CourseLog;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CourseLogService {
    CourseLog addCourseLog(CourseLog courseLog);
    List<CourseLog> findStudentRecentView(String accountId);

    List<CourseLog> findLecturerRecentView(String accountId);
}
