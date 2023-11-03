package fpt.edu.eresourcessystem.repository;

import fpt.edu.eresourcessystem.model.Librarian;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("librarianRepository")
public interface LibrarianRepository extends MongoRepository<Librarian, String> {
    Librarian findByAccountId(String accountId);

    List<Librarian> findLibrarianAndCoursesCreatedBy(String librarianId);

//    @Aggregation(pipeline = {
//            "{ $match: { _id: ?0 } }",
//            "{ $lookup: { from: 'courses', localField: 'createdCourses', foreignField: '_id', as: 'createdCourses' } }"
//    })
//    AggregationResults<Librarian> findLibrarianAndCourses(String librarianId);

    @Aggregation(pipeline = {
            "{ $lookup: { from: 'courses', localField: 'createdCourses', foreignField: '_id', as: 'createdCourses' } }"
    })
    AggregationResults<Librarian> findAllLibrariansAndCourses();
    @Override
    List<Librarian> findAll();
}
