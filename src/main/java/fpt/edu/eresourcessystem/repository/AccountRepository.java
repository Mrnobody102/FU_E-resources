package fpt.edu.eresourcessystem.repository;

import com.mongodb.lang.NonNull;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import fpt.edu.eresourcessystem.model.Account;

import java.util.List;
import java.util.Optional;

@Repository("accountRepository")
public interface AccountRepository extends
        MongoRepository<Account, String> {
    @NotNull
    List<Account> findAll();
    Account findByUsername(String username);

    Account findByEmail(String email);

    @NonNull
    Optional<Account> findById(String accountId);

    void removeAccountByAccountId(String accountId);
    @Query("SELECT FROM Accounts WHERE a.role = 'LECTURER'")
    List<Account> findAllLecturer();

    @Query("SELECT FROM Accounts a WHERE a.role = 'LECTURER' AND (a.username LIKE  ?1  OR " +
            "a.email LIKE ?1 )")
    List<Account> searchLecturer(String search);

//    @Query("SELECT a FROM Account a WHERE a.username LIKE 'huypq1801@gmail.com'")
    Page<Account> findByUsernameLikeOrEmailLike(String username, String email, Pageable pageable);


    @Query("{$and: [{role: ?0}, {$or: [{username: {$regex: ?1}}, {email: {$regex: ?2}}]}]}")
    Page<Account> filterRole(String role, String username, String email, Pageable pageable);
}
