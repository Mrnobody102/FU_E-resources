package fpt.edu.eresourcessystem.service;

import org.springframework.data.domain.Page;
import fpt.edu.eresourcessystem.exception.AccountNotExistedException;
import fpt.edu.eresourcessystem.exception.AccountNotFoundException;
import fpt.edu.eresourcessystem.model.Account;

import java.util.List;

public interface AccountService {
    void addAccount(Account account);
    Account updateAccount(Account account) throws AccountNotExistedException;
    Account findByUsername(String username) throws AccountNotFoundException;
    Account findByEmail(String email) throws AccountNotFoundException;
    List<Account> findAll();
    Page<Account> findAll(int pageIndex, int pageSize, String search);
    boolean deleteById(String id);
    boolean delete(Account product);
    Page<Account> findAll(int pageIndex, int pageSize);
}
