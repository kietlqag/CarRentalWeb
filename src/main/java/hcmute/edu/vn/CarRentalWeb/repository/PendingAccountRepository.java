package hcmute.edu.vn.CarRentalWeb.repository;

import hcmute.edu.vn.CarRentalWeb.entity.Account;
import hcmute.edu.vn.CarRentalWeb.entity.PendingAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PendingAccountRepository extends JpaRepository<PendingAccount, Integer> {
    PendingAccount findByEmail(String email);
    boolean existsByEmail(String email);
    void deleteByEmail(String email);
}
