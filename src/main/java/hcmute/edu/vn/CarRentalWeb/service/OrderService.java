package hcmute.edu.vn.CarRentalWeb.service;

import hcmute.edu.vn.CarRentalWeb.entity.Order;
import hcmute.edu.vn.CarRentalWeb.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepo;

    public List<Order> getAllOrder(){
        return orderRepo.findAll();
    }

    public int countOrderByEmail(String email){
        return orderRepo.countByAccountemail(email);
    }

    public List<Order> getAllOrderByEmail(String email){
        return orderRepo.findAllByAccountemail(email);
    }
}
