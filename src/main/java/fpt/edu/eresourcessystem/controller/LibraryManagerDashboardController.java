package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.common.AccountNotExistedException;
import fpt.edu.eresourcessystem.model.Account;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping
public class LibraryManagerDashboardController {
    @GetMapping("/library-manager/dashboard")
    public String getLibraryManageDashboard(@ModelAttribute Account account) throws AccountNotExistedException {
        return "lib_manager/lib_manager_dashboard";
    }
}
