package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Student;

import java.util.List;

public interface StudentService {
    Student addStudent(Student student);

//    Student findByAccountId(String accountId);

    List<Student> findAll();

    Student findByAccountId(String accountId);

    void updateStudent(Student student);

    boolean checkCourseSaved(String studentId, String courseId);

    boolean saveACourse(String studentId, String CourseId);

    boolean unsavedACourse(String studentId, String courseId);
}
