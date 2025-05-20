package hcmute.edu.vn.CarRentalWeb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ResetPassController {
    @GetMapping("/resetpass")
    public String resetPassPage() {
        return "forgot-password";
    }
}
