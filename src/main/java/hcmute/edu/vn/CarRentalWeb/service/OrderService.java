package hcmute.edu.vn.CarRentalWeb.service;

import hcmute.edu.vn.CarRentalWeb.dto.CheckoutRequest;
import hcmute.edu.vn.CarRentalWeb.entity.Car;
import hcmute.edu.vn.CarRentalWeb.entity.Order;
import hcmute.edu.vn.CarRentalWeb.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public Order getOrderById(int id) {

        return orderRepo.findOrderById(id);
    }
    public List<Order> getRecentOrder() {
        List<Order> order = orderRepo.findAll();
        order.sort((o1, o2) -> o2.getCreatedat().compareTo(o1.getCreatedat()));
        return order.stream().limit(5).collect(Collectors.toList()); // chỉ lấy 5 đơn mới nhất
    }
    public BigDecimal getTotalOfCurrentMonthOrdersByStatus(String status) {
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime startOfNextMonth = startOfMonth.plusMonths(1);

        List<Order> orders = orderRepo.findByCreatedatBetweenAndStatus(startOfMonth, startOfNextMonth, status);

        return orders.stream()
                .map(Order::getTotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void save(Order order ) {

        orderRepo.save(order);
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



            order.setReceivedate(data.getReceiveDate());
            order.setReturndate(data.getReturnDate());
            order.setCountdate(data.getCountDate());

            order.setTotal(data.getTotal());
            order.setDiscount(data.getDiscount());

            orderRepo.save(order);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
