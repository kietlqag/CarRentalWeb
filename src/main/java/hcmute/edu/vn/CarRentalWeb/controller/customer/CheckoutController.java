package hcmute.edu.vn.CarRentalWeb.controller.customer;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class CheckoutController {

    @GetMapping("/checkout")
    public String checkout(@RequestParam("idCar") Long carId, @RequestParam("idService") Long serviceId,
                           @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                           @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                           Model model)
    {

        model.addAttribute("carId", carId);
        model.addAttribute("serviceId", serviceId);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "checkout";
    }

}
