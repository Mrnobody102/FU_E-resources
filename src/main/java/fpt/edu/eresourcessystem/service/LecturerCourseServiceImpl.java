package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.LecturerCourse;
import fpt.edu.eresourcessystem.model.LecturerCourseId;
import fpt.edu.eresourcessystem.repository.LecturerCourseRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("lecturerCourseService")
public class LecturerCourseServiceImpl implements LecturerCourseService{
    private final LecturerCourseRepository lecturerCourseRepository;

    public LecturerCourseServiceImpl(LecturerCourseRepository lecturerCourseRepository) {
        this.lecturerCourseRepository = lecturerCourseRepository;

    }

    @Override
    public LecturerCourse findById(LecturerCourseId lecturerCourseId) {
        Optional<LecturerCourse> lecturer = lecturerCourseRepository.findById(lecturerCourseId);
        return lecturer.orElse(null);
    }

    @Override
    public LecturerCourse add(LecturerCourse lecturerCourse) {
        if(null!=lecturerCourse && null!=lecturerCourse.getLecturerCourseId()){
            if(null!=findById(lecturerCourse.getLecturerCourseId())){
                return null;
            }else {
                LecturerCourse result = lecturerCourseRepository.save(lecturerCourse);
                return result;
            }
        }
        return null;
    }


}
