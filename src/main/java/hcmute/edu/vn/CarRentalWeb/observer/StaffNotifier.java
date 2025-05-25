package hcmute.edu.vn.CarRentalWeb.observer;

import hcmute.edu.vn.CarRentalWeb.entity.Account;
import hcmute.edu.vn.CarRentalWeb.entity.Order;
import hcmute.edu.vn.CarRentalWeb.service.AccountService;
import hcmute.edu.vn.CarRentalWeb.service.CarService;
import hcmute.edu.vn.CarRentalWeb.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StaffNotifier implements OrderObserver{

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private CarService carService;

    @Override
    public void update(Order order) {
        List<Account> staffList = accountService.getByRole("STAFF");
        String carName = carService.getCarById(order.getCarid()).getName();

        String title = "Đơn hàng mới #" + order.getId();
        String message = "Khách hàng " + order.getCustomer() + " vừa tạo đơn hàng. Xe ID: " + order.getCarid() + " " + carName;

        for (Account staff : staffList) {
            notificationService.createNotification(staff.getEmail(), title, message);
        }
    }
}
