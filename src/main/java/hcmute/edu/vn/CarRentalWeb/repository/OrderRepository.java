package hcmute.edu.vn.CarRentalWeb.repository;

import hcmute.edu.vn.CarRentalWeb.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    int countByAccountemail(String accountemail);
    List<Order> findAllByAccountemail(String accountemail);
}
