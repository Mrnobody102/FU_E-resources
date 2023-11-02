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
    @Query("{ 'deleteFlg' : 'PRESERVED' }")
    List<Account> findAll();

    @Query("{$and: [{'deleteFlg' : 'PRESERVED'}, {'username': ?0}]}")
    Account findByUsername(String username);

    Optional<Account> findByEmail(String email);

    @Query("{$and: [{'deleteFlg' : 'PRESERVED'}, {'_id': ?0}]}")
    @NonNull
    Optional<Account> findById(@NonNull String id);

    @Query("SELECT FROM Accounts a WHERE a.role = 'LECTURER' AND a.deleteFlg = 'PRESERVED'")
    List<Account> findAllLecturer();

    @Query("SELECT FROM Accounts a WHERE a.deleteFlg = 'PRESERVED' AND a.role = 'LECTURER' AND (a.username LIKE  ?1  OR " +
            "a.email LIKE ?1 )")
    List<Account> searchLecturer(String search);

    @Query("{$and: [{'deleteFlg' : 'PRESERVED'}, {$or: [{username: {$regex: ?0}}, {email: {$regex: ?1}}]}]}")
    Page<Account> findByUsernameLikeOrEmailLike(String username, String email, Pageable pageable);

    @Query("{$and: [{'deleteFlg' : 'PRESERVED'}, {role: ?0}, {$or: [{username: {$regex: ?1}}, {email: {$regex: ?2}}]}]}")
    Page<Account> filterRole(String role, String username, String email, Pageable pageable);

    void removeAccountById(String id);
}
