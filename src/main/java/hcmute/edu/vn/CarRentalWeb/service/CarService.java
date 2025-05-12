package hcmute.edu.vn.CarRentalWeb.service;

import hcmute.edu.vn.CarRentalWeb.dto.CarRequest;
import hcmute.edu.vn.CarRentalWeb.entity.Car;
import hcmute.edu.vn.CarRentalWeb.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {
    @Autowired
    private CarRepository carRepository;

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car getCarById(int id) {
        return carRepository.findById(id);
    }

    @Transactional
    public void deleteCarById(int id) {
        Car car = carRepository.findById(id);
        if (car == null) {
            throw new RuntimeException("Không tìm thấy xe với ID: " + id);
        }
        try {
            carRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Không thể xóa xe. Có thể xe đang được sử dụng trong hợp đồng: " + e.getMessage());
        }
    }
}
