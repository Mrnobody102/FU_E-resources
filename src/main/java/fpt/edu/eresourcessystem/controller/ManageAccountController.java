package fpt.edu.eresourcessystem.controller;


import fpt.edu.eresourcessystem.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("manager/accounts")
public class ManageAccountController {
    private final AccountService accountService;

    public ManageAccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping({"/list"})
    public String manageAccount(){
        return "lib_manager/lib-manager_accounts";
    }
}
