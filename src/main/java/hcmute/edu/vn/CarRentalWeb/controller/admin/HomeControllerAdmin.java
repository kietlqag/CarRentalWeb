package hcmute.edu.vn.CarRentalWeb.controller.admin;

import hcmute.edu.vn.CarRentalWeb.entity.Account;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeControllerAdmin {
    @GetMapping("/admin/dashboard")
    public String home(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");
        model.addAttribute("account", account);
        return "admin_dashboard";
    }
}
