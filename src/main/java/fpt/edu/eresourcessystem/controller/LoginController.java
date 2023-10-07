package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.enums.AccountEnum;
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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@PropertySources(value = {@PropertySource("classpath:webconfig.properties")})
public class LoginController {
    @Value("${page-size}")
    private Integer pageSize;    private final AccountService accountService;

    public LoginController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping({"/login"})
    public String loginProcess(final Model model) throws AccountNotExistedException {
        model.addAttribute("account", new Account());
        model.addAttribute("roles", AccountEnum.Role.values());
        return "auth/login";
    }

    @PostMapping({"/login"})
    public String login(@ModelAttribute Account account, @RequestParam String roles){
        if(null == account){
            return "redirect:/login";
        } else if ("MANAGER".equals(roles)){
//                account.setRole(AccountEnum.Role.STUDENT);
            return "redirect:/manager";
        } else if ("LECTURER".equals(roles)) {
            return "redirect:/lecturer";
        }else if ("STUDENT".equals(roles)) {
            return "redirect:/student";
        }
        return  "redirect:/guest";
    }

    @GetMapping({"/logout"})
    public String logout(){
        return "redirect:/login";
    }

    @GetMapping({"/", "/guest"})
    public String guest(){
        return "guess/home";
    }

}
