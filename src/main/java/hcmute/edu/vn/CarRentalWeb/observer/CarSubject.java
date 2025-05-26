package hcmute.edu.vn.CarRentalWeb.observer;

import hcmute.edu.vn.CarRentalWeb.entity.Car;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CarSubject{
    private final List<CarObserver> observers = new ArrayList<>();
    public void register(CarObserver observer) {
        observers.add(observer);
    }

    public void notifyAll(Car car) {
        for (CarObserver obs : observers) {
            obs.update(car);
        }
    }
}
