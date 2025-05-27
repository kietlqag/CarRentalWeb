package hcmute.edu.vn.CarRentalWeb.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hcmute.edu.vn.CarRentalWeb.dto.AccountUpdateRequest;
import hcmute.edu.vn.CarRentalWeb.entity.*;
import hcmute.edu.vn.CarRentalWeb.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Map;

@Controller
public class HomeControllerAdmin {
    @Autowired
    private CarService carService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private PromotionService promotionService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ServicesService servicesService;

    private final ObjectMapper objectMapper;
    public HomeControllerAdmin(OrderService orderService, ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/admin/dashboard")
    public String home(HttpSession session, Model model) {

        Account account = (Account) session.getAttribute("account");
        model.addAttribute("account", account);

        if(!account.getRole().equals("ADMIN")) {
            model.addAttribute("errorMessage", "Bạn không có quyền truy cập trang này.");
            return "access-denied";
        }

        List<Car> cars = carService.getAll();
        model.addAttribute("cars", cars);

        List<Account> accounts = accountService.getAllAccount();
        model.addAttribute("accounts", accounts);

        List<Promotion> promotions = promotionService.getAllPromotion();
        model.addAttribute("promotions", promotions);

        List<Order> orders = orderService.getAllOrder();
        model.addAttribute("orders", orders);

        List<Services> services = servicesService.getAllServices();
        model.addAttribute("services", services);

        List<Order> recentOrders = orderService.getRecentOrder();
        model.addAttribute("recentOrders", recentOrders);
        BigDecimal totalThisYear = orderService.getRevenuethisYear();
        model.addAttribute("totalThisYear", totalThisYear);
        int newCustomerYearlyCount = accountService.countNewCustomersThisYear();
        model.addAttribute("newCustomerYearlyCount", newCustomerYearlyCount);
        int countOrderCompleted = orderService.countCompletedOrdersThisYear();
        int countRentedCar= carService.getRentedCarCount();
        model.addAttribute("countRentedCar", countRentedCar);
        model.addAttribute("countOrderCompleted", countOrderCompleted);
        try {
            Map<Integer, BigDecimal> monthlyRevenue = orderService.getMonthlyRevenue();
            String monthlyRevenueJson = objectMapper.writeValueAsString(monthlyRevenue);
            model.addAttribute("monthlyRevenueJson", monthlyRevenueJson);
        } catch (JsonProcessingException e) {
            model.addAttribute("monthlyRevenueJson", "{}");
        }
        return "admin_dashboard";
    }

    @DeleteMapping("/admin/cars/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteCar(@PathVariable int id) {
        carService.deleteCarById(id);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/admin/cars/create")
    @ResponseBody
    public ResponseEntity<String> createCar(@RequestBody Car car) {
        carService.save(car);
        return ResponseEntity.ok("Car created");
    }
    @PutMapping("/admin/cars/update/{id}")
    @ResponseBody
    public ResponseEntity<?> updateCar(@PathVariable int id, @RequestBody Map<String, String> payload) {
        Car car = carService.getCarById(id);
        car.setPrice(Integer.parseInt(payload.get("price")));
        car.setStatus(String.valueOf(payload.get("status")));
        carService.save(car);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/admin/accounts/delete")
    @ResponseBody
    public ResponseEntity<?> deleteAccount(@RequestParam("email") String email) {
            accountService.deleteAccountByEmail(email);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/admin/accounts/update-role")
    @ResponseBody
    public ResponseEntity<?> updateAccountRole(@RequestBody AccountUpdateRequest accountRequest) {
        String email = accountRequest.getEmail();
        String role = accountRequest.getRole();
        accountService.updateRoleAccount(email, role);
        return ResponseEntity.ok("Đã cập nhật vai trò");
    }


    @DeleteMapping("/admin/promotions/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deletePromotion(@PathVariable int id) {
        promotionService.deletePromotionById(id);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/admin/promotions/create")
    @ResponseBody
    public ResponseEntity<String> createCar(@RequestBody Promotion promotion) {
        promotionService.save(promotion);
        return ResponseEntity.ok("Car created");
    }
    @PutMapping("/admin/promotions/update/{id}")
    @ResponseBody
    public ResponseEntity<?> updatePromotion(@PathVariable int id, @RequestBody Map<String, String> payload) {
        Promotion promotion = promotionService.getPromotionById(id);
        promotion.setDiscountpercent(Integer.parseInt(payload.get("discountpercent")));
        promotion.setDescription(payload.get("description"));
        promotion.setValidfrom(Date.valueOf(payload.get("validfrom")));
        promotion.setValidto(Date.valueOf(payload.get("validto")));
        promotion.setCode(payload.get("code"));
        promotion.setType(Integer.parseInt(payload.get("type")));
        promotion.setIsactive(Integer.parseInt(payload.get("isactive")));
        promotionService.save(promotion);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/admin/services/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteService(@PathVariable int id) {
        servicesService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/admin/services/create")
    @ResponseBody
    public ResponseEntity<String> createService(@RequestBody Services service) {
        servicesService.save(service);
        return ResponseEntity.ok("Service created");
    }

    @PutMapping("/admin/services/update/{id}")
    @ResponseBody
    public ResponseEntity<?> updateService(@PathVariable int id, @RequestBody Map<String, String> payload) {
        Services service = servicesService.getServiceById(id);
        service.setNameService(payload.get("nameService"));
        service.setPrice(Integer.parseInt(payload.get("price")));
        service.setStatus(payload.get("status"));
        service.setPicture(payload.get("picture"));
        servicesService.save(service);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/orders/{id}")
    @ResponseBody
    public ResponseEntity<Order> getOrderDetails(@PathVariable int id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }


}
