package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.exception.AccountNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import fpt.edu.eresourcessystem.exception.AccountNotExistedException;
import fpt.edu.eresourcessystem.dto.ObjectRespond;
import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.service.AccountService;

import java.util.List;

@Controller
@RequestMapping("/manage/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/add")
    public ObjectRespond addAccount(Account account){
        accountService.addAccount(account);
        return new ObjectRespond("success", account);
    }

    @PutMapping("/update")
    public ResponseEntity updateAccount(Account account) throws AccountNotExistedException {
        accountService.updateAccount(account);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/list")
    public ResponseEntity<List<Account>> findAll(){
        return ResponseEntity.ok(accountService.findAll());
    }

    @GetMapping("/list/{username}")
    public ObjectRespond findByUserName(@PathVariable String username) throws AccountNotFoundException {
        return new ObjectRespond("success",accountService.findByUsername(username));
    }

    @GetMapping("/list/{pageIndex}")
    Page<Account> findAccountByPage(@PathVariable Integer pageIndex, String search){
        return null;
    }

    @DeleteMapping("/delete")
    public String delete(String id){
        return null;
    }



}
