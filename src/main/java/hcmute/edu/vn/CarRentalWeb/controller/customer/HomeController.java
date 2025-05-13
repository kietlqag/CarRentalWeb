package hcmute.edu.vn.CarRentalWeb.controller.customer;

import hcmute.edu.vn.CarRentalWeb.entity.Account;
import hcmute.edu.vn.CarRentalWeb.entity.Car;
import hcmute.edu.vn.CarRentalWeb.entity.Services;
import hcmute.edu.vn.CarRentalWeb.service.CarService;
import hcmute.edu.vn.CarRentalWeb.service.ServicesService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ServicesService servicesService;
    @Autowired
    private CarService carService;

    @GetMapping("/home")
    public String home(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "0") int carPage,
                       HttpSession session,
                       Model model) {
        Account account = (Account) session.getAttribute("account");
        model.addAttribute("account", account);

        Page<Services> services = servicesService.getServicePage("Hoạt động", PageRequest.of(page, 4));
        model.addAttribute("services", services);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", services.getTotalPages());

        Page<Car> cars = carService.getCarPage("Sẵn sàng", PageRequest.of(carPage, 6));
        model.addAttribute("cars", cars.getContent());
        model.addAttribute("currentCarPage", carPage);
        model.addAttribute("totalCarPages", cars.getTotalPages());

        return "index";
    }

    @GetMapping("/home/services")
    public String getPagedServices(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 4; // mỗi trang 3 service
        Page<Services> servicePage = servicesService.getServicePage("Hoạt động", PageRequest.of(page, pageSize));

        model.addAttribute("services", servicePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", servicePage.getTotalPages());

        return "fragments/service_list_fragment :: serviceList";
    }

    @GetMapping("/home/cars")
    public String getPagedCar(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 6;
        Page<Car> carPage = carService.getCarPage("Sẵn sàng", PageRequest.of(page, pageSize));

        model.addAttribute("cars", carPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", carPage.getTotalPages());

        return "fragments/car_list_fragment :: carList";
    }
}

