package hcmute.edu.vn.CarRentalWeb.decorator;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public interface OrderComponent {
    long calculateTotal();
}
