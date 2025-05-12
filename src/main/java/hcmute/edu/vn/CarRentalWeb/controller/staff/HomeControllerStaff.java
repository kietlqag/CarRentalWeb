package hcmute.edu.vn.CarRentalWeb.controller.staff;

import hcmute.edu.vn.CarRentalWeb.entity.Account;
import hcmute.edu.vn.CarRentalWeb.entity.Order;
import hcmute.edu.vn.CarRentalWeb.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeControllerStaff {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/staff/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");
        model.addAttribute("account", account);

        // Lấy danh sách đơn hàng từ database
        List<Order> orders = orderRepository.findAll();
        model.addAttribute("orders", orders); // truyền vào model để Thymeleaf dùng

        return "staff_dashboard"; // trỏ tới file staff_dashboard.html
    }
}
