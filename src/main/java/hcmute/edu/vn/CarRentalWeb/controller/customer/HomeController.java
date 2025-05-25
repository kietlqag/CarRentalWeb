package hcmute.edu.vn.CarRentalWeb.controller.customer;

import hcmute.edu.vn.CarRentalWeb.entity.Account;
import hcmute.edu.vn.CarRentalWeb.entity.Car;
import hcmute.edu.vn.CarRentalWeb.entity.Services;
import hcmute.edu.vn.CarRentalWeb.service.CarService;
import hcmute.edu.vn.CarRentalWeb.service.ServicesService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
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
                       Model model) throws IOException {

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

        List<String> brandList = carService.getBrandList();
        model.addAttribute("brandList", brandList);

        List<Integer> seatList = carService.getSeatList();
        model.addAttribute("seatList", seatList);

        return "index";
    }

    //Phân trang
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
    public String getPagedCar(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(required = false) String brand,
                              @RequestParam(required = false) Integer seat,
                              @RequestParam(required = false) String price,
                              Model model) {
        int pageSize = 6;

        Integer minPrice = null;
        Integer maxPrice = null;
        if (price != null && !price.isEmpty() && price.contains("-")) {
            String[] parts = price.split("-");
            if (parts.length == 2) {
                try {
                    minPrice = Integer.parseInt(parts[0].trim());
                    maxPrice = Integer.parseInt(parts[1].trim());
                } catch (NumberFormatException e) {
                    System.err.println("Lỗi parse giá: " + e.getMessage());
                }
            }
        }


        Page<Car> carPage = carService.filterCars("Sẵn sàng", brand, seat, minPrice, maxPrice, PageRequest.of(page, pageSize));


        model.addAttribute("cars", carPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalCarPages", carPage.getTotalPages());

        return "fragments/car_list_fragment :: carList";

    }

}

