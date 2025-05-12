package hcmute.edu.vn.CarRentalWeb.controller.admin;

import hcmute.edu.vn.CarRentalWeb.entity.Account;
import hcmute.edu.vn.CarRentalWeb.entity.Car;
import hcmute.edu.vn.CarRentalWeb.service.CarService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class HomeControllerAdmin {
    @Autowired
    private CarService carService;

    @GetMapping("/admin/dashboard")
    public String home(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");
        model.addAttribute("account", account);
        List<Car> cars = carService.getAllCars();
        model.addAttribute("cars", cars);
        return "admin_dashboard";
    }

    @DeleteMapping("/admin/cars/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteCar(@PathVariable int id) {
        carService.deleteCarById(id);
        return ResponseEntity.ok().build();
    }
}
