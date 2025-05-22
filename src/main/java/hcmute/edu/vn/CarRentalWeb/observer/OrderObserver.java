package hcmute.edu.vn.CarRentalWeb.observer;

import hcmute.edu.vn.CarRentalWeb.entity.Car;
import hcmute.edu.vn.CarRentalWeb.entity.Order;

public interface OrderObserver {
    void update(Order order);
}
