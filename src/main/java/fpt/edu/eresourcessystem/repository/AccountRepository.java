package fpt.edu.eresourcessystem.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import fpt.edu.eresourcessystem.model.Account;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends
        MongoRepository<Account, String> {

    @Query("{'name' :  ?0}")
    Optional<Account> findByEmail();

    @Query("{'email':  ?0, 'ISBN':  ?1}")
    List<Account> findByEmailOrISBN(String email, String ISBN);


}
