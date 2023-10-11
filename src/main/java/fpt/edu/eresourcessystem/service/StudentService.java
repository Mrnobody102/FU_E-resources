package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Student;
import fpt.edu.eresourcessystem.model.Topic;

import java.util.List;

public interface StudentService {
    void addStudent(Student student);

    Student findByAccountId(String accountId);

    void updateStudentSavedCourse(Student student);

    boolean checkCourseSaved(String studentId, String courseId);

    boolean saveACourse(String studentId, String CourseId);
}
