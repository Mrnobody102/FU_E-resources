package fpt.edu.eresourcessystem.repository;


import com.mongodb.lang.NonNull;
import fpt.edu.eresourcessystem.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("studentRepository")
public interface StudentRepository extends MongoRepository<Student, String> {
//    @Query("SELECT s FROM students s WHERE s.accountId = ?1")
    Student findByAccountId(String accountId);

    @NonNull
    @Override
    List<Student> findAll();
}
