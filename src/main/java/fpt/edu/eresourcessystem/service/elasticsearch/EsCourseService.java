package fpt.edu.eresourcessystem.service.elasticsearch;

import fpt.edu.eresourcessystem.model.elasticsearch.EsCourse;
import fpt.edu.eresourcessystem.repository.elasticsearch.EsCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EsCourseService {

    private final EsCourseRepository esCourseRepository;

    public Page<EsCourse> searchCourse(String searchTerm, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return esCourseRepository.search(searchTerm, pageable);
    }

    @Autowired
    public EsCourseService(EsCourseRepository esCourseRepository) {
        this.esCourseRepository = esCourseRepository;
    }

    public EsCourse save(EsCourse course) {
        return esCourseRepository.save(course);
    }

    public void delete(EsCourse course) {
        esCourseRepository.delete(course);
    }

    public Optional<EsCourse> findOne(String id) {
        return esCourseRepository.findById(id);
    }

    public Iterable<EsCourse> findAll() {
        return esCourseRepository.findAll();
    }
}
