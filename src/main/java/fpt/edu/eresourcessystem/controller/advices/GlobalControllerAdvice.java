//package fpt.edu.eresourcessystem.controller.advices;
//
//import fpt.edu.eresourcessystem.model.Account;
//import fpt.edu.eresourcessystem.service.AccountService;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ModelAttribute;
//
//@ControllerAdvice
//public class GlobalControllerAdvice {
//
//    private final AccountService accountService;
//
//    public GlobalControllerAdvice(AccountService accountService) {
//        this.accountService = accountService;
//    }
//
//    @ModelAttribute("loggedInUser")
//    public Account addLoggedInUserToModel() {
//        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
//        return accountService.findByEmail(loggedInEmail);
//    }
//}
