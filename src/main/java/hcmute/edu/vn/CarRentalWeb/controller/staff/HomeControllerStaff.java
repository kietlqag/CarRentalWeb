package hcmute.edu.vn.CarRentalWeb.controller.staff;

import hcmute.edu.vn.CarRentalWeb.entity.Account;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeControllerStaff {

    @GetMapping("/staff/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");
        model.addAttribute("account", account);
        return "staff_dashboard";
    }
}
