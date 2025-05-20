package hcmute.edu.vn.CarRentalWeb.service;

import hcmute.edu.vn.CarRentalWeb.dto.AccountUpdateRequest;
import hcmute.edu.vn.CarRentalWeb.entity.Account;
import hcmute.edu.vn.CarRentalWeb.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepo;
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
    public int countNewCustomersThisMonth() {return accountRepo.countAccountsCreatedThisMonth();}
    public int countNewCustomersThisYear(){return accountRepo.countAccountsCreatedThisYear();}

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

}
