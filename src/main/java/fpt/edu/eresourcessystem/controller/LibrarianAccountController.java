package fpt.edu.eresourcessystem.controller;


import fpt.edu.eresourcessystem.dto.ObjectRespond;
import fpt.edu.eresourcessystem.exception.AccountNotExistedException;
import fpt.edu.eresourcessystem.exception.AccountNotFoundException;
import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.service.AccountService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("librarian/accounts")
public class LibrarianAccountController {
    private final AccountService accountService;

    public LibrarianAccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping({"/list"})
    public String manageAccount(final Model model){
        List<Account> accounts = new ArrayList<>();
        model.addAttribute("accounts",null);
        return "librarian/librarian_accounts";
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
