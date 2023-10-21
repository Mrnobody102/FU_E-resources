package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.exception.AccountNotExistedException;
import fpt.edu.eresourcessystem.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InitController {
    @GetMapping({"/", "/guest", "/home"})
    public String goHomePage() {
        return "guest/guest_home";
    }

    @GetMapping({"/access_denied"})
    public String accessDenied() {
        return "exception/404";
    }
}
