package hcmute.edu.vn.CarRentalWeb.repository;

import hcmute.edu.vn.CarRentalWeb.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    boolean existsByEmail(String email);
    Account findByEmail(String email);

    void deleteByEmail(String email);
}
