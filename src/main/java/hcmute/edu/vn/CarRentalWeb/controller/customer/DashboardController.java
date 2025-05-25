package hcmute.edu.vn.CarRentalWeb.controller.customer;

import hcmute.edu.vn.CarRentalWeb.dto.AccountUpdateRequest;
import hcmute.edu.vn.CarRentalWeb.entity.Account;
import hcmute.edu.vn.CarRentalWeb.entity.Notification;
import hcmute.edu.vn.CarRentalWeb.entity.Order;
import hcmute.edu.vn.CarRentalWeb.service.AccountService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
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
    @Autowired
    AccountService accountService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");
        model.addAttribute("account", account);

        if(!account.getRole().equals("CUSTOMER")) {
            model.addAttribute("errorMessage", "Bạn không có quyền truy cập trang này.");
            return "access-denied";
        }

        List<Integer> types = new ArrayList<>();
        types.add(0);
        types.add(account.getRanks());

        int countOrder = orderService.countOrderByEmail(account.getEmail());
        List<Order> Orders = orderService.getAllOrderByEmail(account.getEmail());
        List<Order> ordersfive = orderService.getOrdersByEmail(account.getEmail());
        List<Notification> notificationList = notificationService.getAllNotifications(account.getEmail());
        List<Notification> notificationList3 = notificationService.get3Notifications(account.getEmail());

        long countPromotion = promotionService.countPromotionByTypes(types);

        model.addAttribute("countOrder", countOrder);
        model.addAttribute("Orders", Orders);
        model.addAttribute("ordersfive", ordersfive);
        model.addAttribute("notificationList", notificationList);
        model.addAttribute("countPromotion", countPromotion);
        model.addAttribute("notificationList3", notificationList3);

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

        String receiveDateStr = payload.get("receivedate");
        String returnDateStr = payload.get("returndate");

        order.setNote(payload.get("note"));
        try {
            Date receiveDate = Date.valueOf(receiveDateStr);
            Date returnDate = Date.valueOf(returnDateStr);

            if (!receiveDate.before(returnDate)) {
                return ResponseEntity.badRequest().body("Ngày nhận phải trước ngày trả.");
            }

            order.setReceivedate(Date.valueOf(receiveDateStr));
            order.setReturndate(Date.valueOf(returnDateStr));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi định dạng ngày nhận (receivedate) hoặc trả (returndate)");
        }

        int countdate = orderService.calculateCountDate(Date.valueOf(receiveDateStr), Date.valueOf(returnDateStr));
        BigDecimal total = orderService.updateTotal(countdate, order.getPrice(), order.getServiceprice(), order.getDiscount());
        order.setCountdate(countdate);
        order.setTotal(total);
        orderService.save(order);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/customer/orders/cancel/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable int id) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Đơn hàng không tồn tại.");
        }

        boolean success = orderService.updateStatus(order.getId(), order.getCarid());
        if (success) {
            return ResponseEntity.ok("Đơn hàng đã được huỷ thành công.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Không thể huỷ đơn hàng.");
        }
    }
    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute AccountUpdateRequest dto,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        Account account = (Account) session.getAttribute("account");

        if (account == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Phiên đăng nhập không hợp lệ. Vui lòng đăng nhập lại.");
            return "redirect:/login";
        }
        try {
            accountService.updateProfile(account.getEmail(), dto);
            Account updatedAccount = accountService.getAccountByEmail(account.getEmail());
            session.setAttribute("account", updatedAccount);

            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi cập nhật: " + e.getMessage());
        }

        return "redirect:/dashboard";
    }
}
