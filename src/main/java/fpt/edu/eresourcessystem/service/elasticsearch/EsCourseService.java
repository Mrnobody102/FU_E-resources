package fpt.edu.eresourcessystem.service.elasticsearch;

import fpt.edu.eresourcessystem.model.elasticsearch.EsCourse;
import fpt.edu.eresourcessystem.repository.elasticsearch.EsCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EsCourseService {

    private final EsCourseRepository courseRepository;

    @Autowired
    public EsCourseService(EsCourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public EsCourse save(EsCourse course) {
        return courseRepository.save(course);
    }

    public void delete(EsCourse course) {
        courseRepository.delete(course);
    }

    public Optional<EsCourse> findOne(String id) {
        return courseRepository.findById(id);
    }

    public Iterable<EsCourse> findAll() {
        return courseRepository.findAll();
    }
}
