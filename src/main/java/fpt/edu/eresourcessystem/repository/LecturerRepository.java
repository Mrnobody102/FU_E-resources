package fpt.edu.eresourcessystem.repository;

import fpt.edu.eresourcessystem.model.Lecturer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("lecturerRepository")
public interface LecturerRepository extends MongoRepository<Lecturer, String> {

    Optional<Lecturer> findById(String lecturerId);

    @Override
    List<Lecturer> findAll();

    @Query("SELECT l FROM Course c join c.lecturers l where c.courseId = ?1")
    List<Lecturer> findByCourseId(String courseId);
    @Query("SELECT l FROM Lecturer l WHERE l.accountId = ?1")
    Lecturer findByAccountId(String accountId);
}
