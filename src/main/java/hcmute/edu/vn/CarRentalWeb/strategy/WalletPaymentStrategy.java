package hcmute.edu.vn.CarRentalWeb.strategy;

import hcmute.edu.vn.CarRentalWeb.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class WalletPaymentStrategy implements PaymentStrategy {

    @Override
    public String getName() {
        return "WALLET";
    }

    @Override
    public void pay(Order order) {
        order.setPaymentmethod(getName());
        order.setPaymentstatus("Chưa thanh toán");
    }
}
