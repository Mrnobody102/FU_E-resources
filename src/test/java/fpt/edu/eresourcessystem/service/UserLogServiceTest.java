package fpt.edu.eresourcessystem.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.UserLog;
import fpt.edu.eresourcessystem.repository.UserLogRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@ExtendWith(MockitoExtension.class)
public class UserLogServiceTest {

    @Mock
    private UserLogRepository userLogRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private UserLogServiceImpl userLogService;

    private UserLog userLog;
    private Course course;
    private LocalDateTime startDate, endDate;
    private String email, role, url, urlPrefix;

    @BeforeEach
    void setUp() {
        userLog = new UserLog(); // Initialize with real or mock data
        course = new Course(); // Initialize with real or mock data
        email = "test@example.com";
        role = "student";
        url = "/test/url";
        urlPrefix = "/student/courses/";
        startDate = LocalDateTime.now().minusDays(1);
        endDate = LocalDateTime.now();
    }

    @Test
    public void testFindAll() {
        List<UserLog> mockUserLogs = Arrays.asList(userLog);
        when(userLogRepository.findAll()).thenReturn(mockUserLogs);

        List<UserLog> result = userLogService.findAll();

        assertEquals(mockUserLogs, result);
        verify(userLogRepository, times(1)).findAll();
    }


    @Test
    public void testFindByEmail() {
        List<UserLog> mockUserLogs = Arrays.asList(userLog);
        when(userLogRepository.findByEmail(email)).thenReturn(mockUserLogs);

        List<UserLog> result = userLogService.findByEmail(email);

        assertEquals(mockUserLogs, result);
        verify(userLogRepository, times(1)).findByEmail(email);
    }


    @Test
    public void testSearchLog() {
        List<UserLog> mockUserLogs = Arrays.asList(userLog);
        when(mongoTemplate.find(any(Query.class), eq(UserLog.class))).thenReturn(mockUserLogs);

        List<UserLog> result = userLogService.searchLog(email, role, startDate, endDate);

        assertFalse(result.isEmpty());
        verify(mongoTemplate, times(1)).find(any(Query.class), eq(UserLog.class));
    }

    @Test
    public void testFindByUrl() {
        List<UserLog> mockUserLogs = Arrays.asList(userLog);
        when(userLogRepository.findByUrl(url)).thenReturn(mockUserLogs);

        List<UserLog> result = userLogService.findByUrl(url);

        assertEquals(mockUserLogs, result);
        verify(userLogRepository, times(1)).findByUrl(url);
    }


    @Test
    public void testAddUserLog() {
        when(userLogRepository.save(userLog)).thenReturn(userLog);

        UserLog result = userLogService.addUserLog(userLog);

        assertNotNull(result);
        verify(userLogRepository, times(1)).save(userLog);
    }



//    @Test
//    public void testFindStudentRecentView() {
//        List<String> urlList = Arrays.asList("url1", "url2");
//        List<Course> expectedCourses = Arrays.asList(new Course(), new Course());
//
//        when(mongoTemplate.findDistinct(any(Query.class), eq("url"), eq(UserLog.class), eq(String.class))).thenReturn(urlList);
//        when(mongoTemplate.find(any(Query.class), eq(Course.class))).thenReturn(expectedCourses);
//
//        List<Course> actualCourses = userLogService.findStudentRecentView(email);
//
//        assertEquals(expectedCourses, actualCourses);
//        verify(mongoTemplate, times(1)).findDistinct(any(Query.class), eq("url"), eq(UserLog.class), eq(String.class));
//        verify(mongoTemplate, times(1)).find(any(Query.class), eq(Course.class));
//    }



    @Test
    public void testFindStudentRecentView() {
        // Mock responses for mongoTemplate
        List<String> mockUrls = Arrays.asList(urlPrefix + "course1", urlPrefix + "course2");
        Course course1 = new Course();
        course1.setId("course1"); // Assuming there's a setter for id or relevant field
        Course course2 = new Course();
        course2.setId("course2");

        List<Course> expectedCourses = Arrays.asList(course1, course2);


        when(mongoTemplate.findDistinct(any(Query.class), eq("url"), eq(UserLog.class), eq(String.class))).thenReturn(mockUrls);
        when(mongoTemplate.find(any(Query.class), eq(Course.class))).thenReturn(expectedCourses);

        // Call the method under test
        List<Course> actualCourses = userLogService.findStudentRecentView(email);

        // Assertions
        assertEquals(expectedCourses, actualCourses);
        verify(mongoTemplate, times(1)).findDistinct(any(Query.class), eq("url"), eq(UserLog.class), eq(String.class));
        verify(mongoTemplate, times(1)).find(any(Query.class), eq(Course.class));
    }




}

