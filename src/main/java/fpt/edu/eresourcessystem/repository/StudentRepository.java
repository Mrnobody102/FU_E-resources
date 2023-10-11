package fpt.edu.eresourcessystem.repository;


import fpt.edu.eresourcessystem.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("studentRepository")
public interface StudentRepository extends MongoRepository<Student, String> {
    @Query("SELECT s FROM Studdent s WHERE s.accountId = ?1")
    Student findByAccountId(String accountId);
}
