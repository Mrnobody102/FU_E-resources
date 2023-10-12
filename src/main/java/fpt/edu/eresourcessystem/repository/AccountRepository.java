package fpt.edu.eresourcessystem.repository;

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
    List<Account> findAll();
    Account findByUsername(String username);
    // nên chỉ định rõ kq trả về

    Account findByEmail(String email);

    Optional<Account> findById(String accountId);

    void removeAccountByAccountId(String accountId);
    @Query("SELECT FROM Accounts WHERE a.role = 'LIBRARIAN'")
    List<Account> findAllLecturer();

    @Query("SELECT FROM Accounts WHERE a.role = 'LECTURER' AND (a.username LIKE  ?1  OR " +
            "a.email LIKE ?1 )")
    List<Account> searchLecturer(String search);
//    @Query("SELECT a FROM Account a WHERE a.username LIKE 'huypq1801@gmail.com'")
    Page<Account> findByUsernameLikeOrEmailLike(String username, String email, Pageable pageable);

}
