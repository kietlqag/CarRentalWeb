package hcmute.edu.vn.CarRentalWeb.controller;

import hcmute.edu.vn.CarRentalWeb.singleton.UserSession;
import hcmute.edu.vn.CarRentalWeb.dto.LoginRequest;
import hcmute.edu.vn.CarRentalWeb.entity.Account;
import hcmute.edu.vn.CarRentalWeb.repository.AccountRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@ModelAttribute("loginRequest") LoginRequest loginRequest,
                              HttpSession session,
                              Model model) {

        Account account = accountRepo.findByEmail(loginRequest.getEmail());
        if (account == null || !passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())) {
            model.addAttribute("error", "Email hoặc mật khẩu không đúng.");
            return "login";
        }

        session.setAttribute("account", account);

        UserSession userSession = UserSession.getInstance();
        userSession.setEmail(account.getEmail());
        userSession.setPassword(account.getPassword());
        userSession.setFullName(account.getFullName());
        userSession.setPhone(account.getPhone());
        userSession.setAddress(account.getAddress());
        userSession.setRole(account.getRole());
        userSession.setRanks(account.getRanks());
        userSession.setScore(account.getScore());

        return "redirect:/context";
    }
}
