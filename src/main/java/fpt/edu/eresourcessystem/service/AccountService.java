package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.dto.ObjectRespond;
import org.springframework.data.domain.Page;
import fpt.edu.eresourcessystem.exception.AccountNotExistedException;
import fpt.edu.eresourcessystem.exception.AccountNotFoundException;
import fpt.edu.eresourcessystem.model.Account;

import java.util.List;

public interface AccountService {
    void addAccount(Account account);
    ObjectRespond updateAccount(Account account);
    Account findByUsername(String username) ;
    Account findById(String accountId) ;
    Account findByEmail(String email);
    List<Account> findAll();
    Page<Account> findByPage(int pageIndex, int pageSize, String search);
    boolean deleteById(String id);
    boolean delete(Account product);
    Page<Account> findAll(int pageIndex, int pageSize);
}
