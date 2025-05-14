package hcmute.edu.vn.CarRentalWeb.service;

import hcmute.edu.vn.CarRentalWeb.dto.CheckoutRequest;
import hcmute.edu.vn.CarRentalWeb.entity.Order;
import hcmute.edu.vn.CarRentalWeb.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public boolean saveOrder(CheckoutRequest data){
        try {
            Order order = new Order();
            order.setCustomer(data.getCustomer());
            order.setAccountemail(data.getEmail());
            order.setPhone(data.getPhone());
            order.setPicklocation(data.getPicklocation());
            order.setNote(data.getNote());
            order.setStatus("Chờ xác nhận");
            order.setPaymentstatus("Chưa thanh toán");
            order.setPaymentmethod("CASH");
            order.setCreatedat(LocalDateTime.now());
            order.setName(data.getName());
            order.setService(data.getService() != null ? data.getService() : "Không");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate receiveDate = LocalDate.parse(data.getReceiveDate(), formatter);
            LocalDate returnDate = LocalDate.parse(data.getReturnDate(), formatter);

            order.setReceivedate(receiveDate);
            order.setReturndate(returnDate);
            order.setCountdate(data.getCountDate());

            order.setTotal(data.getTotal());

            orderRepo.save(order);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
