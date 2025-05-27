package hcmute.edu.vn.CarRentalWeb.observer;

import hcmute.edu.vn.CarRentalWeb.entity.Order;
import hcmute.edu.vn.CarRentalWeb.service.CarService;
import hcmute.edu.vn.CarRentalWeb.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusNotifier implements OrderObserver {

    @Autowired
    private NotificationService notificationService;

    @Override
    public void update(Order order) {

        String status = order.getStatus();
        String message = "";
        String title = "";
        if ("Chờ xác nhận".equals(status)) {
            title = "Đơn hàng đang chờ xác nhận";
            message = "Bạn đã đặt đơn hàng #" + order.getId() +
                    ". Chúng tôi sẽ sớm xác nhận đơn hàng của bạn.";
        } else if ("Xác nhận".equals(status)) {
            title = "Đơn hàng đã được xác nhận";
            message = "Đơn hàng #" + order.getId() +
                    " đã được xác nhận. Vui lòng chuẩn bị để nhận xe.";
        } else if ("Đã hoàn thành".equals(status)) {
            title = "Đơn hàng đã hoàn thành";
            message = "Cảm ơn bạn đã sử dụng dịch vụ! Đơn hàng #" + order.getId() + " đã hoàn thành.";
        } else if ("Đã hủy".equals(status)) {
            title = "Đơn hàng đã bị hủy";
            message = "Đơn hàng #" + order.getId() +
                    " đã bị hủy. Nếu có thắc mắc, vui lòng liên hệ hỗ trợ.";
        }
        notificationService.createNotification(order.getAccountemail(), title, message);
    }
}
