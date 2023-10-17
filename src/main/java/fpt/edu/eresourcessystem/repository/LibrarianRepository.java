package fpt.edu.eresourcessystem.repository;

import fpt.edu.eresourcessystem.model.Librarian;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("librarianRepository")
public interface LibrarianRepository extends MongoRepository<Librarian, String> {
    Librarian findByAccountId(String accountId);

    @Override
    List<Librarian> findAll();
}
