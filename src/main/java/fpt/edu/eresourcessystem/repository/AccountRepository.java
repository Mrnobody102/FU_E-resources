package fpt.edu.eresourcessystem.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import fpt.edu.eresourcessystem.model.Account;

import java.util.Optional;

@Repository
public interface AccountRepository extends
        MongoRepository<Account, String> {

    Optional<Account> findByUsername(String username);
    Optional<Account> findByEmail(String email);

}
