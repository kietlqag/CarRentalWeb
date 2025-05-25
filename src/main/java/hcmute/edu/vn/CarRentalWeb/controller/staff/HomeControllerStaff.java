package hcmute.edu.vn.CarRentalWeb.controller.staff;

import hcmute.edu.vn.CarRentalWeb.entity.Account;
import hcmute.edu.vn.CarRentalWeb.entity.Car;
import hcmute.edu.vn.CarRentalWeb.entity.Notification;
import hcmute.edu.vn.CarRentalWeb.entity.Order;
import hcmute.edu.vn.CarRentalWeb.repository.AccountRepository;
import hcmute.edu.vn.CarRentalWeb.repository.NotificationRepository;
import hcmute.edu.vn.CarRentalWeb.repository.OrderRepository;
import hcmute.edu.vn.CarRentalWeb.service.AccountService;
import hcmute.edu.vn.CarRentalWeb.service.NotificationService;
import hcmute.edu.vn.CarRentalWeb.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
public class HomeControllerStaff {

    @Autowired
    private OrderService orderService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/staff/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");
        model.addAttribute("account", account);

        if(!account.getRole().equals("STAFF")) {
            model.addAttribute("errorMessage", "Bạn không có quyền truy cập trang này.");
            return "access-denied";
        }

        List<Order> orders = orderService.getAllOrder();
        model.addAttribute("orders", orders);

        List<Notification> notificationList = notificationService.getAllNotifications(account.getEmail());
        model.addAttribute("notificationList", notificationList);

        int totalOrders = orderService.getAllOrder().size();
        model.addAttribute("totalOrders", totalOrders);

        int pendingOrders = orderService.countOrdersByStatus("Chờ xác nhận");
        model.addAttribute("pendingOrders", pendingOrders);

        return "staff_dashboard";
    }

    @PutMapping("/profile/update")
    @ResponseBody
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, String> payload, HttpSession session) {
        Account account = (Account) session.getAttribute("account");
        if (account == null) return ResponseEntity.status(401).body("Chưa đăng nhập");

        account.setPhone(payload.get("phone"));
        account.setAddress(payload.get("address"));
        accountService.save(account);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/staff/orders/update/{id}")
    @ResponseBody
    public ResponseEntity<?> updateOrder(@PathVariable int id, @RequestBody Map<String, String> payload) {
        Order order = orderService.getOrderById(id);
        String receiveDateStr = payload.get("receivedate");
        String returnDateStr = payload.get("returndate");
        String statusStr = payload.get("status");
        order.setPaymentstatus(payload.get("paymentstatus"));

        try {
            Date receiveDate = Date.valueOf(receiveDateStr); 
            Date returnDate = Date.valueOf(returnDateStr);

            if (!receiveDate.before(returnDate)) {
                return ResponseEntity.badRequest().body("Ngày nhận phải trước ngày trả.");
            }

            order.setReceivedate(receiveDate);
            order.setReturndate(returnDate);

            long countdate = orderService.calculateCountDate(receiveDate, returnDate);
            BigDecimal newtotal = orderService.updateTotal(countdate, order.getPrice(), order.getServiceprice(), order.getDiscount());

            order.setCountdate((int) countdate);
            order.setTotal(newtotal);

            if(statusStr.equals("Đã hủy")) {
                orderService.updateCarStatusForStaff(order.getId(), order.getCarid());
            } else {
                order.setStatus(statusStr);
            }

            orderService.save(order);
            return ResponseEntity.ok().build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("error");
        }
    }


    @GetMapping("/staff/orders/{id}")
    @ResponseBody
    public ResponseEntity<Order> getOrderDetails(@PathVariable int id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }
}
