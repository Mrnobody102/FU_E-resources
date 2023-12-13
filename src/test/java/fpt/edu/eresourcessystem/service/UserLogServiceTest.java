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
import java.util.Collections;
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

    }

    @Test
    public void testFindAll() {
        List<UserLog> mockList = Arrays.asList(new UserLog(), new UserLog());
        when(userLogRepository.findAll()).thenReturn(mockList);

        List<UserLog> result = userLogService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userLogRepository).findAll();
    }

    @Test
    public void testFindByEmail_WithResults() {
        // Arrange
        String email = "hoaltm@fpt.edu.vn";
        UserLog expectedResult = new UserLog();
        when(userLogRepository.findByEmail(email)).thenReturn(Collections.singletonList(expectedResult));

        // Act
        List<UserLog> result = userLogService.findByEmail(email);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertSame(expectedResult, result.get(0));
        verify(userLogRepository).findByEmail(email);
    }


    @Test
    public void testFindByEmail_WithNoResults() {
        // Arrange
        String email = "hoaltm@gmail.com";
        when(userLogRepository.findByEmail(email)).thenReturn(Collections.emptyList());

        // Act
        List<UserLog> result = userLogService.findByEmail(email);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userLogRepository).findByEmail(email);
    }

    @Test
    public void testFindByEmail_NullEmail() {
        // Arrange
        String email = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            userLogService.findByEmail(email);
        });
    }



    @Test
    public void testSearchLog() {
        String email = "hoa@email.com";
        String role = "ADMIN";
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);

        List<UserLog> mockList = Arrays.asList(new UserLog());
        when(mongoTemplate.find(any(Query.class), eq(UserLog.class))).thenReturn(mockList);

        List<UserLog> result = userLogService.searchLog(email, role, startDate, endDate);

        assertNotNull(result);
        verify(mongoTemplate).find(any(Query.class), eq(UserLog.class));
        // Additional assertions as necessary
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



//    @Test
//    public void testFindStudentRecentView() {
//        // Mock responses for mongoTemplate
//        List<String> mockUrls = Arrays.asList(urlPrefix + "course1", urlPrefix + "course2");
//        Course course1 = new Course();
//        course1.setId("course1"); // Assuming there's a setter for id or relevant field
//        Course course2 = new Course();
//        course2.setId("course2");
//
//        List<Course> expectedCourses = Arrays.asList(course1, course2);
//
//
//        when(mongoTemplate.findDistinct(any(Query.class), eq("url"), eq(UserLog.class), eq(String.class))).thenReturn(mockUrls);
//        when(mongoTemplate.find(any(Query.class), eq(Course.class))).thenReturn(expectedCourses);
//
//        // Call the method under test
//        List<Course> actualCourses = userLogService.findStudentRecentView(email);
//
//        // Assertions
//        assertEquals(expectedCourses, actualCourses);
//        verify(mongoTemplate, times(1)).findDistinct(any(Query.class), eq("url"), eq(UserLog.class), eq(String.class));
//        verify(mongoTemplate, times(1)).find(any(Query.class), eq(Course.class));
//    }




}

