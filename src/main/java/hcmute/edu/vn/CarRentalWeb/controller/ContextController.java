package hcmute.edu.vn.CarRentalWeb.controller;

import hcmute.edu.vn.CarRentalWeb.singleton.UserSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContextController {

//    @Autowired
//    UserSession userSession;

    @GetMapping("/context")
    public String context(Model model) {
        UserSession userSession = UserSession.getInstance();
        model.addAttribute("account", userSession);
        if ("ADMIN".equals(userSession.getRole())) {
            return "redirect:/admin/dashboard";
        } else if ("STAFF".equals(userSession.getRole())) {
            return "redirect:/staff/dashboard";
        } else {
            return "redirect:/home";
        }
    }
}
