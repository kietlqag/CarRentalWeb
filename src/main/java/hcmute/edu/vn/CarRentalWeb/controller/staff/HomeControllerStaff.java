package hcmute.edu.vn.CarRentalWeb.controller.staff;

import hcmute.edu.vn.CarRentalWeb.entity.Account;
import hcmute.edu.vn.CarRentalWeb.entity.Car;
import hcmute.edu.vn.CarRentalWeb.entity.Order;
import hcmute.edu.vn.CarRentalWeb.repository.OrderRepository;
import hcmute.edu.vn.CarRentalWeb.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
public class HomeControllerStaff {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;

    @GetMapping("/staff/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");
        model.addAttribute("account", account);

        // Lấy danh sách đơn hàng từ database
        List<Order> orders = orderRepository.findAll();
        model.addAttribute("orders", orders); // truyền vào model để Thymeleaf dùng

        return "staff_dashboard"; // trỏ tới file staff_dashboard.html
    }

    @PutMapping("/staff/orders/update/{id}")
    @ResponseBody
    public ResponseEntity<?> updateOrder(@PathVariable int id, @RequestBody Map<String, String> payload) {
        Order order = orderService.getOrderById(id);

        // Cập nhật thông tin status và paymentstatus
        order.setStatus(payload.get("status"));
        order.setPaymentstatus(payload.get("paymentstatus"));

        // Cập nhật ngày nhận và ngày trả (receivedate, returndate)
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            String receiveDateStr = payload.get("receivedate");
            String returnDateStr = payload.get("returndate");

            if (receiveDateStr != null && !receiveDateStr.isEmpty()) {
                LocalDate localDate = LocalDate.parse(receiveDateStr, inputFormatter);
                order.setReceivedate(Date.valueOf(localDate));
            } else {
                order.setReceivedate(null);
            }

            if (returnDateStr != null && !returnDateStr.isEmpty()) {
                LocalDate localDate = LocalDate.parse(returnDateStr, inputFormatter);
                order.setReturndate(Date.valueOf(localDate));
            } else {
                order.setReturndate(null);
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi định dạng ngày nhận (receivedate) hoặc trả (returndate)");
        }

        // Lưu đơn hàng đã cập nhật
        orderService.save(order);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/staff/orders/{id}")
    @ResponseBody
    public ResponseEntity<Order> getOrderDetails(@PathVariable int id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }
}
