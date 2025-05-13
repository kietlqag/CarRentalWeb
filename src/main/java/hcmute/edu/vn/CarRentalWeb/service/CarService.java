package hcmute.edu.vn.CarRentalWeb.service;

import hcmute.edu.vn.CarRentalWeb.entity.Car;
import hcmute.edu.vn.CarRentalWeb.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CarService {
    @Autowired
    private CarRepository carRepository;

    public List<Car> getAllCars() {

        return carRepository.findAll();
    }

    public Car getCarById(int id) {

        return carRepository.findCarById(id);
    }

    @Transactional
    public void deleteCarById(int id) {
        Car car = carRepository.findCarById(id);
    }
    public void save(Car car) {
        carRepository.save(car);
    }
}
