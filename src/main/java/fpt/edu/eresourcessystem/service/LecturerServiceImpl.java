package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Lecturer;
import fpt.edu.eresourcessystem.repository.LecturerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("lecturerService")
public class LecturerServiceImpl implements LecturerService{
    private LecturerRepository lecturerRepository;

    public LecturerServiceImpl(LecturerRepository lecturerRepository) {
        this.lecturerRepository = lecturerRepository;
    }

    @Override
    public List<Lecturer> findByCourseId(String courseId) {
        List<Lecturer> lecturers = lecturerRepository.findByCourseId(courseId);
        return lecturers;
    }
}
