package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.CourseLog;

import java.util.List;

public interface CourseLogService {
    CourseLog addCourseLog(CourseLog courseLog);
    List<String> findStudentRecentView(String accountId);

    List<CourseLog> findLecturerRecentView(String accountId);
}
