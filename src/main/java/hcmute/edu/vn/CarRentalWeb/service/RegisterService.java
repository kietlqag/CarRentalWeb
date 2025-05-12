package hcmute.edu.vn.CarRentalWeb.service;

import hcmute.edu.vn.CarRentalWeb.dto.RegisterRequest;
import hcmute.edu.vn.CarRentalWeb.entity.Account;
import hcmute.edu.vn.CarRentalWeb.entity.PendingAccount;
import hcmute.edu.vn.CarRentalWeb.repository.AccountRepository;
import hcmute.edu.vn.CarRentalWeb.repository.PendingAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class RegisterService {

    @Autowired
    private PendingAccountRepository pendingRepo;

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean emailExists(String email) {
        return accountRepo.existsByEmail(email) || pendingRepo.existsByEmail(email);
    }

    public boolean handleRegister(RegisterRequest request) {
        try {
            String otp = generateOtp();
            String hashedPassword = passwordEncoder.encode(request.getPassword());

            PendingAccount pending = new PendingAccount();
            pending.setEmail(request.getEmail());
            pending.setPassword(hashedPassword);
            pending.setFullName(request.getFullName());
            pending.setOtp(otp);
            pending.setOtpExpireTime(LocalDateTime.now().plusMinutes(5));

            pendingRepo.save(pending);
            sendOtpEmail(request.getEmail(), otp);
            return true;
        } catch (Exception e) {
            System.out.println("Đăng ký thất bại: " + e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean verifyOtp(String email, String otp) {
        PendingAccount pending = pendingRepo.findByEmail(email);

        if (pending == null || pending.getOtp() == null || pending.getOtp().isEmpty()) {
            return false;
        }

        if (pending.getOtpExpireTime().isBefore(LocalDateTime.now())) {
            return false;
        }

        if (!pending.getOtp().equals(otp)) {
            return false;
        }

        Account account = new Account();
        account.setEmail(pending.getEmail());
        account.setPassword(pending.getPassword());
        account.setFullName(pending.getFullName());
        account.setRole("CUSTOMER");

        accountRepo.save(account);
        pendingRepo.deleteByEmail(email);

        return true;
    }

    private String generateOtp() {
        return String.valueOf(new Random().nextInt(900000) + 100000); // Mã 6 số
    }

    private void sendOtpEmail(String toEmail, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Xác minh đăng ký - Mã OTP");
            message.setText("Mã OTP của bạn là: " + otp + ". OTP có hiệu lực trong 5 phút.");
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("Gửi email thất bại: " + e.getMessage());
        }
    }

    public boolean resendOtp(String email) {
        PendingAccount pending = pendingRepo.findByEmail(email);

        if (pending == null) return false;

        String otp = generateOtp();
        LocalDateTime expireTime = LocalDateTime.now().plusMinutes(5);

        pending.setOtp(otp);
        pending.setOtpExpireTime(expireTime);
        pendingRepo.save(pending);

        sendOtpEmail(email, otp);

        return true;
    }
}
