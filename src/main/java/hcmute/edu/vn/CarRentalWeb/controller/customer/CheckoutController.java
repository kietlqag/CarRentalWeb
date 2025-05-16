package hcmute.edu.vn.CarRentalWeb.controller.customer;

import hcmute.edu.vn.CarRentalWeb.dto.CheckoutRequest;
import hcmute.edu.vn.CarRentalWeb.entity.Car;
import hcmute.edu.vn.CarRentalWeb.entity.Account;
import hcmute.edu.vn.CarRentalWeb.entity.Promotion;
import hcmute.edu.vn.CarRentalWeb.entity.Services;
import hcmute.edu.vn.CarRentalWeb.service.CarService;
import hcmute.edu.vn.CarRentalWeb.service.OrderService;
import hcmute.edu.vn.CarRentalWeb.service.PromotionService;
import hcmute.edu.vn.CarRentalWeb.service.ServicesService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CheckoutController {

    @Autowired
    private CarService carService;
    @Autowired
    private ServicesService servicesService;
    @Autowired
    private PromotionService promotionService;
    @Autowired
    private OrderService orderService;

    @GetMapping("/checkout")
    public String checkout(@RequestParam("idCar") int carId, @RequestParam("idService") int serviceId,
                           @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                           @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                           Model model,
                           HttpSession session)
    {
        List<Integer> types = new ArrayList<>();
        types.add(0);

        if (session != null) {
            Account account = (Account) session.getAttribute("account");
            if (account != null) {
                model.addAttribute("account", account);
                types.add(account.getRanks()); // thêm rank nếu có account
            }
        }

        Car car = carService.getCarById(carId);
        Services service = servicesService.getServiceById(serviceId);
        List<Promotion> promotionList = promotionService.getAllPromotionByTypes(types);
        long countdate = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        int total = (int) countdate * car.getPrice();
        if (service != null) {
            total += service.getPrice();
        }

        model.addAttribute("car", car);
        model.addAttribute("service", service);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("countdate", countdate);
        model.addAttribute("total", total);
        model.addAttribute("promotionList", promotionList);

        return "checkout";
    }


    @PostMapping("/checkout/save")
    public String saveCheckout(@ModelAttribute CheckoutRequest checkoutData, Model model) {

        boolean isSuccess = orderService.saveOrder(checkoutData);

        if (isSuccess) {
            model.addAttribute("customerName", checkoutData.getCustomer());
            return "checkout-success";
        } else {
            model.addAttribute("errorMessage", "Có lỗi xảy ra khi lưu đơn đặt xe. Vui lòng thử lại.");
            return "redirect:/";
        }
    }

}
