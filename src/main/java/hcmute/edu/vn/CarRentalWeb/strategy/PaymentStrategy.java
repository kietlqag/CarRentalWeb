package hcmute.edu.vn.CarRentalWeb.strategy;

import hcmute.edu.vn.CarRentalWeb.entity.Order;

public interface PaymentStrategy {
    String getName();
    void pay(Order order);
}
