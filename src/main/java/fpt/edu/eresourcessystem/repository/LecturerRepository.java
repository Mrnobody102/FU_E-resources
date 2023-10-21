package fpt.edu.eresourcessystem.repository;

import com.mongodb.lang.NonNull;
import fpt.edu.eresourcessystem.model.Lecturer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("lecturerRepository")
public interface LecturerRepository extends MongoRepository<Lecturer, String> {

    @NonNull
    Optional<Lecturer> findById(@NonNull String lecturerId);

    @NonNull
    @Override
    List<Lecturer> findAll();

    @Query("SELECT l FROM lecturers c join c.lecturers l where c.lecturerId = ?1")
    List<Lecturer> findByLecturerId(String lecturerId);
    @Query("SELECT l FROM lecturers l WHERE l.accountId = ?1")
    Lecturer findByAccountId(String accountId);
}
