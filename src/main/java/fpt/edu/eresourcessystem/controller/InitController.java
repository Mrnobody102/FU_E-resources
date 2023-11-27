package fpt.edu.eresourcessystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InitController {
    @GetMapping({"/", "/guest", "/home"})
    public String goHomePage() {
        return "guest/guest_home";
    }

    @GetMapping({"/contact_us"})
    public String viewContactUs(){
        return "guest/guest_contact-us";
    }

    @GetMapping({"/faq"})
    public String viewFAQ(){
        return "guest/guest_help-faqs";
    }

    @GetMapping({"/access_denied"})
    public String accessDenied() {
        return "exception/404";
    }
}
