package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Student;
import fpt.edu.eresourcessystem.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("studentService")
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student addStudent(Student student) {
        if (null == student.getAccountId()) {
            Student result = studentRepository.save(student);
            return result;
        } else {
            Student checkExist = studentRepository.findByAccountId(student.getAccountId());
            if (null != checkExist) {
                Student result = studentRepository.save(student);
                return result;
            }
        }
        return null;
    }

    @Override
    public Student findByAccountId(String accountId) {
        Student student = studentRepository.findByAccountId(accountId);
        return student;
    }

    @Override
    public List<Student> findAll() {
        List<Student> students = studentRepository.findAll();
        return students;
    }

    @Override
    public void updateStudentSavedCourse(Student student) {
        studentRepository.save(student);
    }

    @Override
    public boolean checkCourseSaved(String studentId, String courseId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isPresent()) {
            Student existedStudent = student.get();
            if (null != existedStudent.getSavedCourses()) {
                for (String cId : existedStudent.getSavedCourses()) {
                    if (cId.equals(courseId)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean saveACourse(String studentId, String courseId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (!student.isPresent()) {
            return false;
        }
        List<String> savedCourses = student.get().getSavedCourses();
        // check student have saved any course
        if (null == savedCourses) {
            savedCourses = new ArrayList<>();
        }
        // check course existed in saved course
        for (String cId : savedCourses) {
            if (courseId.equals(cId)) {
                return false;
            }
        }
        savedCourses.add(courseId);
        student.get().setSavedCourses(savedCourses);
        updateStudentSavedCourse(student.get());
        return true;
    }

    @Override
    public boolean unsavedACourse(String studentId, String courseId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (!student.isPresent()) {
            return false;
        }
        List<String> savedCourses = student.get().getSavedCourses();
        // check student have saved any course
        if (null == savedCourses) {
            savedCourses = new ArrayList<>();
        }
        // check course existed in saved course and delete
        for (String cId : savedCourses) {
            if (courseId.equals(cId)) {
                savedCourses.remove(cId);
                student.get().setSavedCourses(savedCourses);
                updateStudentSavedCourse(student.get());
                return true;
            }
        }
        return false;
    }
}
