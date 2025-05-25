package hcmute.edu.vn.CarRentalWeb.controller;

import hcmute.edu.vn.CarRentalWeb.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ResetPassController {

    @Autowired
    AccountService accountService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password"; // nhập email
    }

    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam("email") String email, Model model) {
        boolean success = accountService.sendResetPasswordLink(email);
        if (success) {
            model.addAttribute("message", "Đã gửi liên kết đến email của bạn.");
        } else {
            model.addAttribute("error", "Email không tồn tại trong hệ thống.");
        }
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        boolean valid = accountService.isResetTokenValid(token);
        if (!valid) {
            model.addAttribute("error", "Liên kết không hợp lệ hoặc đã hết hạn.");
            return "forgot-password";
        }
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(
            @RequestParam("token") String token,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model
    ) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu xác nhận không khớp.");
            model.addAttribute("token", token);
            return "reset-password";
        }

        boolean result = accountService.updatePassword(token, password);
        if (result) {
            model.addAttribute("message", "Mật khẩu đã được cập nhật.");
        } else {
            model.addAttribute("error", "Đã có lỗi xảy ra. Vui lòng thử lại.");
        }
        model.addAttribute("token", token);
        return "reset-password";
    }

}
