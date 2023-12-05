package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Student;
import fpt.edu.eresourcessystem.repository.StudentRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
//@RunWith(MockitoJUnitRunner.class)
public class StudentServiceTest {
    public MockMvc mockMvc;
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    void setUp() throws Exception {
//        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
    }



//    @Test
//    public void updateStudentSuccess(){
//
//    }
//
//    @Test
//    void addStudentSuccess(){
//        Student student = Student.builder()
//
//                .build();
//    }

    @Test
    void testCheckCourseSaved_WhenCourseIsSaved_ReturnsTrue() {
        String studentId = "656d0a88a5e9120287158b41"; //email: minhvb@fpt.edu.vn- Vũ Bảo Minh
        String courseId = "656d21b95302664d15b91fa2";  //PRF192 đã đánh bookmark

        Student student = new Student();
        student.setSavedCourses(Arrays.asList("656d21b95302664d15b91fa2"));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        boolean result = studentService.checkCourseSaved(studentId, courseId);
        assertTrue(result);
    }

    @Test
    void testCheckCourseSaved_WhenCourseIsNotSaved_ReturnsFalse() {
        String studentId = "656d0a88a5e9120287158b41";//email: minhvb@fpt.edu.vn- Vũ Bảo Minh
        String courseId = "656cec35b2b347634e153d2e4";//MLN111
        Student student = new Student();
        student.setSavedCourses(Arrays.asList("656d21b95302664d15b91fa2"));

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        boolean result = studentService.checkCourseSaved(studentId, courseId);
        assertFalse(result);
    }

    @Test
    void testCheckCourseSaved_WhenStudentNotFound_ReturnsFalse() {
        String studentId = "656d0a88a5e9120287158b41c";//student k tồn tại
        String courseId = "656d21b95302664d15b91fa2"; //PRF192
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
        boolean result = studentService.checkCourseSaved(studentId, courseId);
        assertFalse(result);
    }

    @Test
    void testCheckDocSaved_WhenDocIsSaved_ReturnsTrue() {
        String studentId = "656d0a61a5e9120287158b3f"; //Vũ Minh Anh
        String docId = "656d20575302664d15b91f9f";
        Student student = new Student();
        student.setSavedDocuments(Arrays.asList(docId));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        boolean result = studentService.checkDocSaved(studentId, docId);
        assertTrue(result);
    }

    @Test
    void testCheckDocSaved_WhenDocIsNotSaved_ReturnsFalse() {
        String studentId = "656d0a61a5e9120287158b3f"; //Vũ Minh Anh
        String docId = "656d20575302664d15b91f9f";
        Student student = new Student();
        student.setSavedDocuments(Arrays.asList("656d22475302664d15b91fa8"));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        boolean result = studentService.checkDocSaved(studentId, docId);
        assertFalse(result);
    }

    @Test
    void testCheckDocSaved_WhenStudentNotFound_ReturnsFalse() {
        String studentId = "656d0a61a5e9120287158b3f"; //Vũ Minh Anh
        String docId = "656d20575302664d15b91f9f";
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
        boolean result = studentService.checkDocSaved(studentId, docId);
        assertFalse(result);
    }

    @Test
    void testSaveACourse_WhenStudentDoesNotExist_ReturnsFalse() {
        String studentId = "656d0a88a5e9120287158b41c";
        String courseId = "656d21b95302664d15b91fa2";
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
        boolean result = studentService.saveACourse(studentId, courseId);
        assertFalse(result);
        verify(studentRepository, times(1)).findById(studentId);
    }

    @Test
    void testSaveACourse_WhenCourseAlreadySaved_ReturnsFalse() {
        String studentId = "656d0a88a5e9120287158b41";
        String courseId = "656d21b95302664d15b91fa2";
        Student student = new Student();
        List<String> savedCourses = new ArrayList<>(List.of(courseId));
        student.setSavedCourses(savedCourses);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        boolean result = studentService.saveACourse(studentId, courseId);
        assertFalse(result);
        verify(studentRepository, times(1)).findById(studentId);

    }

    @Test
    void testSaveACourse_WhenCourseNotSaved_ReturnsTrue() {
        String studentId = "656d0a88a5e9120287158b41";
        String courseId = "656d21b95302664d15b91fa2";
        Student student = new Student();
        List<String> savedCourses = new ArrayList<>();
        student.setSavedCourses(savedCourses);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        boolean result = studentService.saveACourse(studentId, courseId);
        assertTrue(result);
        verify(studentRepository, times(1)).findById(studentId);
    }

}
