package hcmute.edu.vn.CarRentalWeb.observer;

import hcmute.edu.vn.CarRentalWeb.entity.Order;
import hcmute.edu.vn.CarRentalWeb.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CarStatusUpdater implements OrderObserver {
    @Autowired
    private CarRepository carRepo;

    @Override
    public void update(Order order) {
        carRepo.findById(order.getCarid()).ifPresent(car -> {
            car.setStatus("Đang thuê");
            carRepo.save(car);
        });
    }
}
