package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.model.Account;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InitController {
    @GetMapping({ "/guest"})
    public String goGuestHomePage() {
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
    @GetMapping({"/bad_request"})
    public String badRequest() {
        return "exception/404";
    }
}
