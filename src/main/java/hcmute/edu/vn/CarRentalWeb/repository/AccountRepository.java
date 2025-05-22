package hcmute.edu.vn.CarRentalWeb.repository;

import hcmute.edu.vn.CarRentalWeb.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    boolean existsByEmail(String email);
    Account findByEmail(String email);
    List<Account> findByRole(String role);



    void deleteByEmail(String email);
    @Query("SELECT COUNT(a) FROM Account a WHERE a.role = 'CUSTOMER' AND YEAR(a.createdate) = YEAR(CURRENT_DATE)")
    int countAccountsCreatedThisYear();
}
