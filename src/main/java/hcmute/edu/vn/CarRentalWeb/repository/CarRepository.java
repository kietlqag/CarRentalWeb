package hcmute.edu.vn.CarRentalWeb.repository;

import hcmute.edu.vn.CarRentalWeb.entity.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {

    Car findCarById(int id);
    List<Car> findAllByStatus(String status);
    Page<Car> findAllByStatus(String status, Pageable pageable);

    @Query("SELECT DISTINCT c.brand FROM Car c")
    List<String> findDistinctBrands();

    @Query("SELECT DISTINCT c.seat FROM Car c")
    List<Integer> findDistinctSeats();

    @Query("SELECT c FROM Car c " +
            "WHERE c.status = :status " +
            "AND (:brand IS NULL OR :brand = '' OR c.brand = :brand) " +
            "AND (:seat IS NULL OR c.seat = :seat) " +
            "AND (:minPrice IS NULL OR c.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR c.price <= :maxPrice)")
    Page<Car> filterCars(@Param("status") String status,
                         @Param("brand") String brand,
                         @Param("seat") Integer seat,
                         @Param("minPrice") Integer minPrice,
                         @Param("maxPrice") Integer maxPrice,
                         Pageable pageable);
    int countByStatus(String status);




}
