package hcmute.edu.vn.CarRentalWeb.controller.customer;

import hcmute.edu.vn.CarRentalWeb.Car;
import hcmute.edu.vn.CarRentalWeb.entity.Account;
import hcmute.edu.vn.CarRentalWeb.entity.Services;
import hcmute.edu.vn.CarRentalWeb.service.CarService;
import hcmute.edu.vn.CarRentalWeb.service.ServicesService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Controller
public class CheckoutController {

    @Autowired
    private CarService carService;
    @Autowired
    private ServicesService servicesService;

    @GetMapping("/checkout")
    public String checkout(@RequestParam("idCar") int carId, @RequestParam("idService") int serviceId,
                           @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                           @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                           Model model,
                           HttpSession session)
    {

        if (session != null) {
            Account account = (Account) session.getAttribute("account");
            if (account != null) {
                model.addAttribute("account", account); // Gán vào model nếu có
            }
        }

        Car car = carService.getCarById(carId);
        Services service = servicesService.getServiceById(serviceId);
        long countdate = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        long total = countdate * car.getPrice();
        if (service != null) {
            total += service.getPrice();
        }


        model.addAttribute("car", car);
        model.addAttribute("service", service);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("countdate", countdate);
        model.addAttribute("total", total);

        return "checkout";
    }

}
