package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.Librarian;
import fpt.edu.eresourcessystem.repository.CourseRepository;
import fpt.edu.eresourcessystem.repository.LibrarianRepository;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("librarianService")
public class LibrarianServiceImpl implements LibrarianService {
    private final LibrarianRepository librarianRepository;

    public LibrarianServiceImpl(LibrarianRepository librarianRepository) {
        this.librarianRepository = librarianRepository;
    }

    @Override
    public Librarian addLibrarian(Librarian librarian) {
        return librarianRepository.insert(librarian);
    }

    @Override
    public Librarian updateLibrarian(Librarian librarian) {
        Librarian result = librarianRepository.save(librarian);
        return result;
    }

    @Override
    public AggregationResults<Librarian> findLibrarianAndCourses() {
        return librarianRepository.findAllLibrariansAndCourses();
    }

//    public List<Librarian> findAllLibrariansWithCourses() {
//        return librarianRepository.findAllLibrariansAndCourses();
//    }

    @Override
    public Librarian findByAccountId(String accountId) {
        Librarian librarian = librarianRepository.findByAccountId(accountId);
        return librarian;
    }


}
