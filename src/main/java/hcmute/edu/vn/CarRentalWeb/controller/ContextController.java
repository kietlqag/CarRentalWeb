package hcmute.edu.vn.CarRentalWeb.controller;

import hcmute.edu.vn.CarRentalWeb.entity.Account;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContextController {

    @GetMapping("/context")
    public String context(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");
        model.addAttribute("account", account);
        if ("ADMIN".equals(account.getRole())) {
            return "redirect:/admin/dashboard";
        } else if ("STAFF".equals(account.getRole())) {
            return "redirect:/staff/dashboard";
        } else {
            return "redirect:/home";
        }
    }
}
