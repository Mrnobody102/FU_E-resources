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
@RequestMapping("/user")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping({"/profile"})
    public String profile(){
        return "common/profile";
    }




}
