package hcmute.edu.vn.CarRentalWeb.observer;

import hcmute.edu.vn.CarRentalWeb.entity.Order;
import hcmute.edu.vn.CarRentalWeb.service.CarService;
import hcmute.edu.vn.CarRentalWeb.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CustomerNotifier implements OrderObserver{

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private CarService carService;

    @Override
    public void update(Order order) {

        String carName = carService.getCarById(order.getCarid()).getName();
        String title = "Đặt xe thành công";
        String message = "Bạn đã đặt đơn hàng #" + order.getId() + " cho xe: " + carName +
                ". Chúng tôi sẽ sớm xác nhận đơn hàng của bạn.";

        notificationService.createNotification(order.getAccountemail(), title, message);
    }
}
