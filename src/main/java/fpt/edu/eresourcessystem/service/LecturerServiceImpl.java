package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.Lecturer;
import fpt.edu.eresourcessystem.model.LecturerCourse;
import fpt.edu.eresourcessystem.repository.LecturerCourseRepository;
import fpt.edu.eresourcessystem.repository.LecturerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("lecturerService")
public class LecturerServiceImpl implements LecturerService {
    private final LecturerRepository lecturerRepository;
    private final CourseService courseService;

    private final LecturerCourseRepository lecturerCourseRepository;

    public LecturerServiceImpl(LecturerRepository lecturerRepository, CourseService courseService, LecturerCourseRepository lecturerCourseRepository) {
        this.lecturerRepository = lecturerRepository;
        this.courseService = courseService;
        this.lecturerCourseRepository = lecturerCourseRepository;
    }

    @Override
    public List<Lecturer> findByCourseId(String courseId) {
        return lecturerRepository.findByLecturerId(courseId);
    }

    @Override
    public Lecturer addLecturer(Lecturer lecturer) {
        Lecturer checkExist = lecturerRepository.findByAccountId(lecturer.getAccountId());
        if (null == checkExist) {
            return lecturerRepository.save(lecturer);
        }
        return null;
    }

    @Override
    public Lecturer updateLecturer(Lecturer lecturer) {
        Optional<Lecturer> checkExist = lecturerRepository.findById(lecturer.getLecturerId());
        if (!checkExist.isPresent()) {
            return null;
        }
        return lecturerRepository.save(lecturer);
    }

    @Override
    public List<Course> findListManageCourse(Lecturer lecturer) {
        Optional<Lecturer> checkExist = lecturerRepository.findById(lecturer.getLecturerId());
        if (checkExist.isPresent()) {
            if (null == checkExist.get().getLecturerCourses()) {
                return null;
            }
            return courseService.findByListId(checkExist.get().getLecturerCourses());
        }
        return null;
    }

    @Override
    public Lecturer findByAccountId(String accountId) {
        return lecturerRepository.findByAccountId(accountId);
    }

    @Override
    public List<Lecturer> findByListLecturerIds(List<String> ids) {
        List<Lecturer> lecturers = lecturerRepository.findByLecturerIds(ids);
        return lecturers;
    }

    @Override
    public Lecturer findCurrentCourseLecturer(String courseId) {
        LecturerCourse lecturerCourse = lecturerCourseRepository.findCurrentCourseLecturer(courseId);
        if(null!= lecturerCourse){
            if(null!=lecturerCourse.getLecturerCourseId().getLecturerId()){
                Optional<Lecturer> lecturer = lecturerRepository.findById(
                        lecturerCourse.getLecturerCourseId().getLecturerId());
                return lecturer.orElse(null);
            }
        }
        return null;
    }

    @Override
    public List<Lecturer> findAll() {
        List<Lecturer> lecturers = lecturerRepository.findAll();
        return lecturers;
    }


}
