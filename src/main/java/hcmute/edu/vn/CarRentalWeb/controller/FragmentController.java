package hcmute.edu.vn.CarRentalWeb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FragmentController {

    @GetMapping("/staff_sb.html")
    public String getStaffSidebar() {
        return "fragments/staff_sb :: sidebar";
    }

    @GetMapping("/admin_sb.html")
    public String getAdminSidebar() {
        return "fragments/admin_sb :: sidebar";
    }

    @GetMapping("/customer_sb.html")
    public String getCustomerSidebar() {
        return "fragments/customer_sb :: sidebar";
    }
}
