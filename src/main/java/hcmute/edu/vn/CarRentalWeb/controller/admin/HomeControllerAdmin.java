package hcmute.edu.vn.CarRentalWeb.controller.admin;

import hcmute.edu.vn.CarRentalWeb.entity.Account;
import hcmute.edu.vn.CarRentalWeb.entity.Car;
import hcmute.edu.vn.CarRentalWeb.service.AccountService;
import hcmute.edu.vn.CarRentalWeb.service.CarService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class HomeControllerAdmin {
    @Autowired
    private CarService carService;
    @Autowired
    private AccountService accountService;

    @GetMapping("/admin/dashboard")
    public String home(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");
        model.addAttribute("account", account);
        List<Car> cars = carService.getAll();
        model.addAttribute("cars", cars);
        List<Account> accounts = accountService.getAllAccount();
        model.addAttribute("accounts", accounts);
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
    @PutMapping("/admin/accounts/update/{email}")
    @ResponseBody
   public ResponseEntity<?> updateAccount(@PathVariable String email, @RequestBody Map<String, String> payload) {
        Account account = accountService.getAccountByEmail(payload.get("email"));
        account.setFullName(payload.get("fullName"));
        account.setAddress(payload.get("address"));
        account.setPhone(payload.get("phone"));
        account.setRole(payload.get("role"));
        accountService.save(account);
        return ResponseEntity.ok().build();
    }

}
