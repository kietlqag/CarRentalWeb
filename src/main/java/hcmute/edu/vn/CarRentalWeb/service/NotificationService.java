package hcmute.edu.vn.CarRentalWeb.service;

import hcmute.edu.vn.CarRentalWeb.entity.Notification;
import hcmute.edu.vn.CarRentalWeb.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getAllNotifications(String email) {
        return notificationRepository.findAllByAccountemail(email);
    }

    public List<Notification> get3Notifications(String email) {
        return notificationRepository.findTop3ByAccountemailOrderByCreatedatDesc(email);
    }

    public void createNotification(String email, String title, String message) {
        Notification noti = new Notification();
        noti.setAccountemail(email);
        noti.setTitle(title);
        noti.setMessage(message);
        noti.setCreatedat(new java.util.Date());
        notificationRepository.save(noti);
    }
}
