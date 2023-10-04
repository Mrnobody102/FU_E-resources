package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.exception.AccountNotExistedException;
import fpt.edu.eresourcessystem.model.Account;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping
public class LibraryManagerController {
    @GetMapping("/manager")

    public String getLibraryManageDashboard(@ModelAttribute Account account) throws AccountNotExistedException {
        return "lib_manager/lib-manager_dashboard";
    }
}
