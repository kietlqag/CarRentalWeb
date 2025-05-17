package hcmute.edu.vn.CarRentalWeb.controller.customer;

import hcmute.edu.vn.CarRentalWeb.entity.Account;
import hcmute.edu.vn.CarRentalWeb.entity.Notification;
import hcmute.edu.vn.CarRentalWeb.entity.Order;
import hcmute.edu.vn.CarRentalWeb.service.NotificationService;
import hcmute.edu.vn.CarRentalWeb.service.OrderService;
import hcmute.edu.vn.CarRentalWeb.service.PromotionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class DashboardController {

    @Autowired
    OrderService orderService;
    @Autowired
    NotificationService notificationService;
    @Autowired
    PromotionService promotionService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");
        model.addAttribute("account", account);

        List<Integer> types = new ArrayList<>();
        types.add(0);
        types.add(account.getRanks());

        int countOrder = orderService.countOrderByEmail(account.getEmail());
        List<Order> Orders = orderService.getAllOrderByEmail(account.getEmail());
        List<Notification> notificationList = notificationService.getAllNotifications(account.getEmail());

        long countPromotion = promotionService.countPromotionByTypes(types);

        model.addAttribute("countOrder", countOrder);
        model.addAttribute("Orders", Orders);
        model.addAttribute("notificationList", notificationList);
        model.addAttribute("countPromotion", countPromotion);

        return "customer_dashboard";
    }

    @GetMapping("/customer/orders/{id}")
    @ResponseBody
    public ResponseEntity<Order> getOrderDetails(@PathVariable int id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/customer/orders/update/{id}")
    @ResponseBody
    public ResponseEntity<?> updateOrder(@PathVariable int id, @RequestBody Map<String, String> payload) {
        Order order = orderService.getOrderById(id);

        order.setNote(payload.get("note"));
        try {
            String receiveDateStr = payload.get("receivedate");
            String returnDateStr = payload.get("returndate");
            order.setReceivedate(Date.valueOf(receiveDateStr));
            order.setReturndate(Date.valueOf(returnDateStr));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi định dạng ngày nhận (receivedate) hoặc trả (returndate)");
        }
        orderService.save(order);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/customer/orders/cancel/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable int id) {
        Optional<Order> optionalOrder = Optional.ofNullable(orderService.getOrderById(id));
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus("Đã huỷ");
            orderService.save(order);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Đơn hàng không tồn tại.");
        }
    }

}
