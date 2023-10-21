package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.Lecturer;
import fpt.edu.eresourcessystem.repository.LecturerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("lecturerService")
public class LecturerServiceImpl implements LecturerService {
    private final LecturerRepository lecturerRepository;
    private final CourseService courseService;

    public LecturerServiceImpl(LecturerRepository lecturerRepository, CourseService courseService) {
        this.lecturerRepository = lecturerRepository;
        this.courseService = courseService;
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
    public List<Course> findListManageCourse(Lecturer lecturer) {
        Optional<Lecturer> checkExist = lecturerRepository.findById(lecturer.getAccountId());
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


}
