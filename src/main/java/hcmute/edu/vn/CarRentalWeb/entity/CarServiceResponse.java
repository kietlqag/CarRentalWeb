package hcmute.edu.vn.CarRentalWeb.entity;

public class CarServiceResponse {
    private Car car;
    private Services service;

    public CarServiceResponse(Car car, Services service) {
        this.car = car;
        this.service = service;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Services getService() {
        return service;
    }

    public void setService(Services service) {
        this.service = service;
    }
}
