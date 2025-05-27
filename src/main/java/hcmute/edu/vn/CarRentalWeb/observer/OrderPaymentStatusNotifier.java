package hcmute.edu.vn.CarRentalWeb.observer;

import hcmute.edu.vn.CarRentalWeb.entity.Order;
import hcmute.edu.vn.CarRentalWeb.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Observer;

@Component
public class OrderPaymentStatusNotifier implements OrderObserver {
    @Autowired
    private NotificationService notificationService;

    @Override
    public void update(Order order) {

        String PaymentStatus = order.getPaymentstatus();
        String message = "";
        String title = "";
        if ("Đã thanh toán".equals(PaymentStatus)) {
            title = "Đơn hàng đã thanh toán";
            message = "Đơn hàng #" + order.getId() +
                    ". đã thanh toán thành công.";
        }
        notificationService.createNotification(order.getAccountemail(), title, message);
    }
}
