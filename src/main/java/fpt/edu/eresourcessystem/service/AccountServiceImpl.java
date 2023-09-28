package fpt.edu.eresourcessystem.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import fpt.edu.eresourcessystem.exception.AccountNotExistedException;
import fpt.edu.eresourcessystem.exception.AccountNotFoundException;
import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.repository.AccountRepository;

import java.util.List;


@Service("accountService")
public class AccountServiceImpl implements AccountService{
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void addAccount(Account account) {
        accountRepository.insert(account);
    }

    @Override
    public Account updateAccount(Account account) throws AccountNotExistedException {
        Account savedAccount = accountRepository.findById(account.getAccountId())
                .orElseThrow(() -> new AccountNotExistedException("Account Not Existed."));
        savedAccount.setUsername(account.getUsername());
        savedAccount.setPassword(account.getPassword());
        savedAccount.setEmail(account.getEmail());
        savedAccount.setRole(account.getRole());
        return accountRepository.save(savedAccount);
    }

    @Override
    public Account findByUsername(String username) throws  AccountNotFoundException{
        // nên validate trước khi throw luôn
        return accountRepository.findByUsername(username).orElseThrow(
                () -> new AccountNotFoundException("Account not existed.")
        );
    }

    @Override
    public Account findByEmail(String email) throws  AccountNotFoundException{
        return accountRepository.findByEmail(email).orElseThrow(
                () -> new AccountNotFoundException("Account not existed.")
        );
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Page<Account> findAll(int pageIndex, int pageSize, String search) {
        return null;
    }

    @Override
    public boolean deleteById(String id) {
        return false;
    }

    @Override
    public boolean delete(Account product) {
        return false;
    }

    @Override
    public Page<Account> findAll(int pageIndex, int pageSize) {
        return null;
    }
}
