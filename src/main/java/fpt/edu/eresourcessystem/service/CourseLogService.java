package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.CourseLog;

import java.util.List;

public interface CourseLogService {
    CourseLog addCourseLog(CourseLog courseLog);
    void deleteCourseLog(CourseLog courseLog);
    List<String> findLecturerRecentView(String email);
    List<CourseLog> findByCourseCodeOrCodeName(String search);
}
