package hcmute.edu.vn.CarRentalWeb.repository;

import hcmute.edu.vn.CarRentalWeb.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findAllByAccountemailOrderByCreatedatDesc(String email);
    List<Notification> findTop3ByAccountemailOrderByCreatedatDesc(String email);
    void deleteNotificationByAccountemail(String accountemail);

}

