package hcmute.edu.vn.CarRentalWeb.service;

import hcmute.edu.vn.CarRentalWeb.entity.Car;
import hcmute.edu.vn.CarRentalWeb.observer.CarNewNotifier;
import hcmute.edu.vn.CarRentalWeb.observer.CarStatusUpdater;
import hcmute.edu.vn.CarRentalWeb.observer.CarSubject;
import hcmute.edu.vn.CarRentalWeb.repository.CarRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private CarNewNotifier carNewNotifier;
    @Autowired
    private CarSubject carSubject;
    @PostConstruct
    public void initObservers() {
        carSubject.register(carNewNotifier);
    }
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
        carSubject.notifyAll(car);
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
    public int getRentedCarCount() {
        return carRepository.countByStatus("Đang thuê");
    }

}
