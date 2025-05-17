package hcmute.edu.vn.CarRentalWeb.repository;

import hcmute.edu.vn.CarRentalWeb.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    boolean existsByEmail(String email);
    Account findByEmail(String email);
    void deleteByEmail(String email);
    @Query("SELECT COUNT(a) FROM Account a WHERE a.role = 'CUSTOMER' AND MONTH(a.createdate) = MONTH(CURRENT_DATE) AND YEAR(a.createdate) = YEAR(CURRENT_DATE)")
    int countAccountsCreatedThisMonth();
}
