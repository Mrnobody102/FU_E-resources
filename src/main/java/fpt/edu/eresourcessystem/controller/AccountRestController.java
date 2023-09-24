package fpt.edu.eresourcessystem.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import fpt.edu.eresourcessystem.common.AccountNotExistedException;
import fpt.edu.eresourcessystem.dto.ObjectRespond;
import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.service.AccountService;

import java.util.List;

@RestController
@RequestMapping("/resourse/accounts")
public class AccountRestController {
    private final AccountService accountService;

    public AccountRestController(AccountService accountService) {
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

    @GetMapping("/list/{pageIndex}")
    Page<Account> findAccountByPage(@PathVariable Integer pageIndex, String search){
        return null;
    }
    @DeleteMapping("delete")
    public String delete(String id){
        return null;
    }


}
