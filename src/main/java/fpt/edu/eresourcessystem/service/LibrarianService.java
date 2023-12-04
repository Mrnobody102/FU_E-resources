package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.Librarian;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import java.util.List;

public interface LibrarianService {
    List<Librarian> findAll();
    Librarian addLibrarian(Librarian librarian);
    Librarian updateLibrarian(Librarian librarian);

    Librarian findByAccountId(String accountId);
    public AggregationResults<Librarian> findLibrarianAndCourses();



//     List<Librarian> findAllLibrariansWithCourses();


}
