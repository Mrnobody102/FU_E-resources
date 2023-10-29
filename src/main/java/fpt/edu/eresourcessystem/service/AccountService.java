package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.dto.AccountDTO;
import fpt.edu.eresourcessystem.model.Account;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AccountService {

    List<Account> findAllLecturer();
    List<Account> searchLecturer(String search);
    Account addAccount(AccountDTO accountDTO);
    Account updateAccount(Account account);
    Account findByUsername(String username) ;
    Account findById(String id);
    Account findByEmail(String email);
    List<Account> findAll();
    Page<Account> findByUsernameLikeOrEmailLike(String username, String email, int pageIndex, int pageSize);

    Page<Account> findByRoleAndUsernameLikeOrEmailLike(String role, String username, String email, int pageIndex, int pageSize);

    boolean delete(Account account);
    Page<Account> findAll(int pageIndex, int pageSize);

}
