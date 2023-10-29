package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.dto.AccountDTO;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service("accountService")
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Account> findAllLecturer() {
        return accountRepository.findAllLecturer();
    }

    @Override
    public List<Account> searchLecturer(String search) {
        return accountRepository.searchLecturer(search);
    }

    @Override
    public Account addAccount(AccountDTO accountDTO) {
        Account account = new Account(accountDTO);
        accountRepository.insert(account);
        return account;
    }

    @Override
    public Account updateAccount(Account account) {
        Optional<Account> savedAccount = accountRepository.findById(account.getId());
        if (savedAccount.isPresent()) {
            return accountRepository.save(account);
        }
        return null;
    }

    @Override
    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    @Override
    public Account findById(String accountId) {
        Optional<Account> account = accountRepository.findById(accountId);
        return account.orElse(null);
    }

    @Override
    public Account findByEmail(String email) {
        Optional<Account> account = accountRepository.findByEmail(email);
        return account.orElse(null);
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Page<Account> findByUsernameLikeOrEmailLike(String username, String email, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        return accountRepository.findByUsernameLikeOrEmailLike(username, email,
                pageable);
    }

    @Override
    public Page<Account> findByRoleAndUsernameLikeOrEmailLike(String role, String username, String email, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        return accountRepository.filterRole(role, username, email,
                pageable);
    }

    @Override
    public boolean delete(Account account) {
        if (accountRepository.findById(account.getId()).isPresent()) {
            account.setDeleteFlg(CommonEnum.DeleteFlg.DELETED);
            accountRepository.save(account);
            return true;
        }
        return false;
    }

    @Override
    public Page<Account> findAll(int pageIndex, int pageSize) {
        return null;
    }
}
