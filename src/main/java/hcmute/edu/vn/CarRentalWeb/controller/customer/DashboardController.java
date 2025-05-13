package hcmute.edu.vn.CarRentalWeb.controller.customer;

import hcmute.edu.vn.CarRentalWeb.entity.Account;
import hcmute.edu.vn.CarRentalWeb.entity.Order;
import hcmute.edu.vn.CarRentalWeb.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    OrderService orderService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");
        model.addAttribute("account", account);

        int countOrder = orderService.countOrderByEmail(account.getEmail());
        List<Order> Orders = orderService.getAllOrderByEmail(account.getEmail());

        model.addAttribute("countOrder", countOrder);
        model.addAttribute("Orders", Orders);

        return "customer_dashboard";
    }
}
