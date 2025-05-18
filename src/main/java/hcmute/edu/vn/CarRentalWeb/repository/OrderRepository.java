package hcmute.edu.vn.CarRentalWeb.repository;

import hcmute.edu.vn.CarRentalWeb.entity.Car;
import hcmute.edu.vn.CarRentalWeb.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Order findOrderById(int id);
    int countByAccountemail(String accountemail);
    List<Order> findAllByAccountemail(String accountemail);
    List<Order> findByCreatedatBetweenAndStatus(LocalDateTime start, LocalDateTime end, String status);
    int countByStatus(String status);
}
