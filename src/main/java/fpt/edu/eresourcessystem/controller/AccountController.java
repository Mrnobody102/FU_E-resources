package fpt.edu.eresourcessystem.controller;


import fpt.edu.eresourcessystem.dto.ObjectRespond;
import fpt.edu.eresourcessystem.enums.AccountEnum;
import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.service.AccountService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("librarian/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping({"/list"})
    public String manageAccount(final Model model){
        List<Account> accounts = new ArrayList<>();
        accounts = accountService.findAll();
        model.addAttribute("accounts",accounts);
        return "librarian/account/librarian_accounts";
    }
    @GetMapping("/add")
    public String addProcess(@ModelAttribute Account account, final Model model){
        if(null!=account){
            model.addAttribute("account",account);
        }else {
            model.addAttribute("account", new Account());
        }
        model.addAttribute("roles", AccountEnum.Role.values());
        model.addAttribute("campuses", AccountEnum.Campus.values());
        model.addAttribute("genders", AccountEnum.Gender.values());
        return "librarian/account/librarian_accounts";

    }
    @PostMapping("/add")
    public String addAccount(@ModelAttribute Account account, final Model model){
        Account checkExist = accountService.findByEmail(account.getEmail());
        if(checkExist!=null){
            return "redirect:/librarian/accounts/add?error";
        }
        accountService.addAccount(account);
        RedirectAttributes attributes = new RedirectAttributesModelMap();
        attributes.addFlashAttribute("account", account);
        return "redirect:/librarian/accounts/add?success";
    }

    @GetMapping("/update")
    public ResponseEntity updateAccount(Account account){
//        accountService.updateAccount(account);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/list/{username}")
    public ObjectRespond findByUserName(@PathVariable String username) {
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
