package hcmute.edu.vn.CarRentalWeb.controller;

import hcmute.edu.vn.CarRentalWeb.dto.RegisterRequest;
import hcmute.edu.vn.CarRentalWeb.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("registerRequest") RegisterRequest request, Model model) {
        if (registerService.emailExists(request.getEmail())) {
            model.addAttribute("error", "Email đã tồn tại!");
            return "register";
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            model.addAttribute("error", "Mật khẩu không khớp!");
            return "register";
        }

        if (registerService.handleRegister(request)) {
            return "redirect:/verify?email=" + request.getEmail();
        } else {
            model.addAttribute("error", "Đăng ký không thành công!");
            return "register";
        }
    }

    @GetMapping("/verify")
    public String verifyOtp(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);
        return "confirm-otp";
    }

    @PostMapping("/verify")
    public String verifyOtp(@RequestParam String email, @RequestParam String otp, RedirectAttributes redirectAttributes) {
        boolean result = registerService.verifyOtp(email, otp);
        if (!result) {
            redirectAttributes.addFlashAttribute("message", "Mã không hợp lệ hoặc đã hết hạn!");
            return "redirect:/verify?email=" + email;
        } else {
            redirectAttributes.addFlashAttribute("message", "Xác nhận thành công!");
            return "redirect:/login";
        }
    }

    @GetMapping("/resend-otp")
    public String resendOtp(@RequestParam String email, RedirectAttributes redirectAttributes) {
        boolean success = registerService.resendOtp(email);

        if (success) {
            redirectAttributes.addFlashAttribute("message", "Mã OTP mới đã được gửi.");
        } else {
            redirectAttributes.addFlashAttribute("message", "Không thể gửi lại mã OTP. Vui lòng kiểm tra email.");
        }
        return "redirect:/verify?email=" + email;
    }
}
