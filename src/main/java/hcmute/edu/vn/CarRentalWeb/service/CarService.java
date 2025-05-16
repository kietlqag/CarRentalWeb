package hcmute.edu.vn.CarRentalWeb.service;

import hcmute.edu.vn.CarRentalWeb.entity.Car;
import hcmute.edu.vn.CarRentalWeb.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    @Autowired
    private CarRepository carRepository;
    public List<Car> getAll() {
        return carRepository.findAll();
    }

    public List<Car> getAllCars() {

        return carRepository.findAllByStatus("Sẵn sàng");
    }

    public Car getCarById(int id) {

        return carRepository.findCarById(id);
    }

    public void deleteCarById(int id) {

        carRepository.deleteById(id);
    }

    public Page<Car> getCarPage(String status, Pageable pageable) {
        return carRepository.findAllByStatus(status, pageable);
    }

    public void save(Car car) {

        carRepository.save(car);
    }

    public List<String> getBrandList(){
        return carRepository.findDistinctBrands();
    }

    public List<Integer> getSeatList(){
        return carRepository.findDistinctSeats();
    }

    public Page<Car> filterCars(String status, String brand, Integer seat, Integer minPrice, Integer maxPrice, Pageable pageable) {

        return carRepository.filterCars(status, brand, seat, minPrice, maxPrice, pageable);

    }

}
