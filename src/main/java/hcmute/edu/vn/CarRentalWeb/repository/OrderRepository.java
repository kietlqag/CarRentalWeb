package hcmute.edu.vn.CarRentalWeb.repository;

import hcmute.edu.vn.CarRentalWeb.entity.Car;
import hcmute.edu.vn.CarRentalWeb.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Order findOrderById(int id);
    int countByAccountemail(String accountemail);
    int countByStatus(String status);
    List<Order> findAllByAccountemailOrderByCreatedatDesc(String accountemail);
    @Query("SELECT SUM(o.total) FROM Order o " +
            "WHERE YEAR(o.createdat) = YEAR(CURRENT_DATE) " +
            "AND o.status = 'Đã hoàn thành'")
    BigDecimal getYearlyRevenueByStatus();
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'Đã hoàn thành' AND YEAR(o.createdat) = YEAR(CURRENT_DATE)")
    int countCompletedOrdersThisYear();
    @Query("SELECT MONTH(o.createdat) as month, SUM(o.total) as total " +
            "FROM Order o " +
            "WHERE YEAR(o.createdat) = :year AND o.status = :status " +
            "GROUP BY MONTH(o.createdat)")
    List<Object[]> findMonthlyRevenueByYearAndStatus(@Param("year") int year, @Param("status") String status);
    List<Order> findTop5ByOrderByCreatedatDesc();
}
