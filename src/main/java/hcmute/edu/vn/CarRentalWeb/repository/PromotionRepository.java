package hcmute.edu.vn.CarRentalWeb.repository;

import hcmute.edu.vn.CarRentalWeb.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {

    @Query("SELECT p FROM Promotion p WHERE p.type IN :types")
    List<Promotion> findAllByTypeIn(@Param("types") List<Integer> types);

    @Query("SELECT COUNT(p) FROM Promotion p WHERE p.type IN :types")
    long countByTypeIn(@Param("types") List<Integer> types);


}
