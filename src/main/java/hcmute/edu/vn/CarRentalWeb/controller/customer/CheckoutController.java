package hcmute.edu.vn.CarRentalWeb.controller.customer;

import hcmute.edu.vn.CarRentalWeb.dto.CheckoutRequest;
import hcmute.edu.vn.CarRentalWeb.entity.Car;
import hcmute.edu.vn.CarRentalWeb.entity.Account;
import hcmute.edu.vn.CarRentalWeb.entity.Promotion;
import hcmute.edu.vn.CarRentalWeb.entity.Services;
import hcmute.edu.vn.CarRentalWeb.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
    @Autowired
    private AccountService accountService;

    @GetMapping("/checkout")
    public String checkout(@RequestParam("idCar") int carId,
                           @RequestParam(value = "idService", required = false, defaultValue = "-1") int serviceId,
                           @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                           @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
                           Model model,
                           HttpSession session) {

        List<Integer> types = new ArrayList<>();
        types.add(0);

        if (session != null) {
            Account account = (Account) session.getAttribute("account");
            if (account != null) {
                model.addAttribute("account", account);
                types.add(account.getRanks());
            }
        }

        Car car = carService.getCarById(carId);

        Services service = null;
        Integer servicePrice = 0;

        if (serviceId != -1) {
            service = servicesService.getServiceById(serviceId);
            if (service != null) {
                servicePrice = service.getPrice();
            }
        }

        List<Promotion> promotionList = promotionService.getAllPromotionByTypes(types);
        long countdate = orderService.calculateCountDate(startDate, endDate);
        long total = orderService.calculateTotal(countdate, car.getPrice(), servicePrice);

        model.addAttribute("car", car);
        model.addAttribute("service", service); // có thể là null
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("countdate", countdate);
        model.addAttribute("total", total);
        model.addAttribute("promotionList", promotionList);

        return "checkout";
    }


    @PostMapping("/checkout/save")
    public String saveCheckout(@ModelAttribute CheckoutRequest checkoutData, Model model, HttpSession session, RedirectAttributes redirect) {

        Account account = (Account) session.getAttribute("account");

        if (account == null) {
            if (accountService.emailExists(checkoutData.getEmail())) {
                redirect.addFlashAttribute("errorMessage", "Email đã được đăng ký tài khoản, vui lòng đăng nhập hoặc nhập email khác để đặt xe.");

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String start = sdf.format(checkoutData.getReceiveDate());
                String end = sdf.format(checkoutData.getReturnDate());

                return "redirect:/checkout?idCar=" + checkoutData.getCarid()
                        + "&idService=" + checkoutData.getServiceid()
                        + "&startDate=" + start
                        + "&endDate=" + end;
            }
        }

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
