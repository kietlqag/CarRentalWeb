package hcmute.edu.vn.CarRentalWeb.controller.admin;

import hcmute.edu.vn.CarRentalWeb.entity.Account;
<<<<<<< HEAD
import hcmute.edu.vn.CarRentalWeb.entity.Car;
import hcmute.edu.vn.CarRentalWeb.entity.Promotion;
=======
>>>>>>> e69a1e10f6ed1e6f9ab33af9ff7f0319e1ee37a7
import hcmute.edu.vn.CarRentalWeb.service.AccountService;
import hcmute.edu.vn.CarRentalWeb.service.CarService;
import hcmute.edu.vn.CarRentalWeb.service.PromotionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/admin/dashboard")
    public String home(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");
        model.addAttribute("account", account);
        List<Car> cars = carService.getAll();
        model.addAttribute("cars", cars);
        List<Account> accounts = accountService.getAllAccount();
        model.addAttribute("accounts", accounts);
        List<Promotion> promotions = promotionService.getAllPromotion();
        model.addAttribute("promotions", promotions);
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
        System.out.println("👉 Received DELETE for email: " + email);
            accountService.deleteAccountByEmail(email);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/admin/accounts/update-role")
    @ResponseBody
    public ResponseEntity<?> updateAccountRole(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String role = payload.get("role");
        Account account = accountService.getAccountByEmail(email);
        account.setRole(role);
        accountService.save(account);
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
        promotion.setCode(payload.get("code"));
        promotion.setType(Integer.parseInt(payload.get("type")));
        promotion.setIsactive(Integer.parseInt(payload.get("isactive")));
        promotionService.save(promotion);
        return ResponseEntity.ok().build();
    }


}
