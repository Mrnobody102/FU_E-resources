package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.dto.ObjectRespond;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.repository.AccountRepository;

import java.util.List;
import java.util.Optional;


@Service("accountService")
public class AccountServiceImpl implements AccountService{
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Account> findAllLecturer() {
        List<Account> lecturers = accountRepository.findAllLecturer();
        return lecturers;
    }

    @Override
    public List<Account> searchLecturer(String search) {
        List<Account> lecturers = accountRepository.searchLecturer("%" + search + "%");
        return lecturers;
    }

    @Override
    public void addAccount(Account account) {
        accountRepository.insert(account);
    }

    @Override
    public Account updateAccount(Account account){
        Optional<Account> savedAccount = accountRepository.findById(account.getAccountId());
        if(savedAccount.isPresent()){
            Account result = accountRepository.save(account);
            return result;
        }
        return null;
    }

    @Override
    public Account findByUsername(String username){
        Account account = accountRepository.findByUsername(username);
        return account;
    }

    @Override
    public Account findByAccountId(String accountId) {
        Optional<Account> account = accountRepository.findById(accountId);
        return account.orElse(null);
    }

    @Override
    public Account findByEmail(String email) {
        Account account = accountRepository.findByEmail(email);
        return account;
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Page<Account> findByPage(int pageIndex, int pageSize, String search) {
        return null;
    }

    @Override
    public boolean deleteById(String accountId) {
        if(accountRepository.existsById(accountId)){
            accountRepository.removeAccountByAccountId(accountId);
            return true;
        }return false;
    }

    @Override
    public boolean delete(Account account) {
        if(accountRepository.findById(account.getAccountId()).isPresent()){
            accountRepository.delete(account);
            return true;
        }
        return false;
    }

    @Override
    public Page<Account> findAll(int pageIndex, int pageSize) {
        return null;
    }
}
