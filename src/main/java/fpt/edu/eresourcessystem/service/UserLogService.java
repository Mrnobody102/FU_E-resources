package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.UserLog;

import java.time.LocalDateTime;
import java.util.List;

public interface UserLogService {
    List<UserLog> findAll();
    List<UserLog> findByEmail(String email);
    List<UserLog> searchLog(String email, String role, LocalDateTime startDate, LocalDateTime endDate);
    List<UserLog> findByUrl(String url);
    UserLog addUserLog(UserLog userLog);

    List<Course> findStudentRecentView(String accountId);
}
