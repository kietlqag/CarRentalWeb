package hcmute.edu.vn.CarRentalWeb.service;

import hcmute.edu.vn.CarRentalWeb.entity.OrderDetail;
import hcmute.edu.vn.CarRentalWeb.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService {
    @Autowired
    private OrderDetailRepository OrderDetailRepo;

    public List<OrderDetail> getOrderDetailList(){
        return OrderDetailRepo.findAll();
    }
}
