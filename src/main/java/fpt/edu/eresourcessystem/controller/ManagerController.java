package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.model.Account;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping
public class ManagerController {
    @GetMapping("/manager")
    public String getLibraryManageDashboard(@ModelAttribute Account account) {
        return "manager/manager_dashboard";
    }
}
