package fpt.edu.eresourcessystem.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import fpt.edu.eresourcessystem.service.AccountService;
import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.exception.AccountNotExistedException;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@PropertySources(value = {@PropertySource("classpath:webconfig.properties")})
public class LoginController {
    @Value("${page-size}")
    private Integer pageSize;    private final AccountService accountService;

    public LoginController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping({"/","/login"})
    public String loginProcess(final Model model) throws AccountNotExistedException {
        model.addAttribute("account", new Account());
        return "auth/login";
    }

    @PostMapping({"/login"})
    public String login(@ModelAttribute Account account){
            if(null == account){
                return "auth/login";
            } else if("MANAGER".equals(account.getUsername())){

                return "lib_manager/lib-manager_dashboard";
            } else if ("LECTURER".equals(account.getUsername())) {
                return "lecturer/lecturer_subjects";
            }
            return  "student/home";
    }

    @GetMapping({"/logout"})
    public String logout(final Model model){
        model.addAttribute("account", new Account());
        return "auth/login";
    }

    @GetMapping({"/profile"})
    public String profile(){
        return "common/profile";
    }
}
