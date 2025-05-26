package hcmute.edu.vn.CarRentalWeb.observer;

import hcmute.edu.vn.CarRentalWeb.entity.Account;
import hcmute.edu.vn.CarRentalWeb.entity.Car;
import hcmute.edu.vn.CarRentalWeb.entity.Order;
import hcmute.edu.vn.CarRentalWeb.service.AccountService;
import hcmute.edu.vn.CarRentalWeb.service.CarService;
import hcmute.edu.vn.CarRentalWeb.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CarNewNotifier implements CarObserver{
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private AccountService accountService;
    @Override
    public void update(Car car) {
        List<Account> customerList = accountService.getByRole("CUSTOMER");
        String title = "Xe Mới Vừa Cập Bến – Khám Phá Ngay!";
        String message = "🎉 Xe Mới Về Bãi: "+car.getName()+" Đã Sẵn Sàng Cho Thuê!";

        for (Account customer : customerList) {
            notificationService.createNotification(customer.getEmail(), title, message);
        }
    }
}
