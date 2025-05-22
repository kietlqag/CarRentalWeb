package hcmute.edu.vn.CarRentalWeb.service;

import hcmute.edu.vn.CarRentalWeb.dto.AccountUpdateRequest;
import hcmute.edu.vn.CarRentalWeb.entity.Account;
import hcmute.edu.vn.CarRentalWeb.entity.PasswordResetToken;
import hcmute.edu.vn.CarRentalWeb.repository.AccountRepository;
import hcmute.edu.vn.CarRentalWeb.repository.PasswordResetTokenRepository;
import hcmute.edu.vn.CarRentalWeb.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {
    @Autowired
    NotificationRepository notificationRepo;
    @Autowired
    AccountRepository accountRepo;
    @Autowired
    PasswordResetTokenRepository tokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender mailSender;
    public boolean emailExists(String email) {
        return accountRepo.existsByEmail(email);
    }
    public List<Account> getAllAccount() {
        return accountRepo.findAll();
    }
    public void save(Account account) {
        accountRepo.save(account);
}
    @Transactional
    public void deleteAccountByEmail(String email) {
        notificationRepo.deleteNotificationByAccountemail(email);
        accountRepo.deleteByEmail(email);
    }
    public Account getAccountByEmail(String email) {
        return accountRepo.findByEmail(email);
}

    public void updateRoleAccount(String email,String role) {
        Account account = accountRepo.findByEmail(email);
        account.setRole(role);
        accountRepo.save(account);
    }

    public int countNewCustomersThisYear() {return accountRepo.countAccountsCreatedThisYear();}

    public void updateProfile(String email, AccountUpdateRequest dto) {
        Account account = accountRepo.findByEmail(email);

        if (account == null) {
            throw new UsernameNotFoundException("Không tìm thấy tài khoản");
        }

        account.setFullName(dto.getFullName());
        account.setAddress(dto.getAddress());
        account.setPhone(dto.getPhone());

        accountRepo.save(account);
    }

    public boolean sendResetPasswordLink(String email) {
        Account account = accountRepo.findByEmail(email);
        if (account == null) return false;

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, email);
        tokenRepository.save(resetToken);

        String link = "http://localhost:5000/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Đặt lại mật khẩu");
        message.setText("Nhấn vào đây để đặt lại mật khẩu: " + link);

        mailSender.send(message);
        return true;
    }


    public boolean isResetTokenValid(String token) {
        return tokenRepository.findByToken(token)
                .filter(t -> t.getExpiryDate().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    public boolean updatePassword(String token, String newPassword) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);
        if (tokenOpt.isEmpty()) return false;

        PasswordResetToken resetToken = tokenOpt.get();

        // Lấy email từ token
        String email = resetToken.getAccountEmail();

        // Tìm Account bằng email
        Optional<Account> accountOpt = Optional.ofNullable(accountRepo.findByEmail(email));
        if (accountOpt.isEmpty()) return false;

        Account account = accountOpt.get();
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepo.save(account);

        tokenRepository.delete(resetToken);
        return true;
    }

    public List<Account> getByRole(String role) {
        return accountRepo.findByRole(role);
    }

}
