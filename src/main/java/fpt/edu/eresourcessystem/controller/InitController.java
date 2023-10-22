package fpt.edu.eresourcessystem.controller;

import org.springframework.stereotype.Controller;
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
