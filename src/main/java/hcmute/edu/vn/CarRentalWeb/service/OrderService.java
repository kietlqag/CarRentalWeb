package hcmute.edu.vn.CarRentalWeb.service;

import hcmute.edu.vn.CarRentalWeb.dto.CheckoutRequest;
import hcmute.edu.vn.CarRentalWeb.entity.Car;
import hcmute.edu.vn.CarRentalWeb.entity.Order;
import hcmute.edu.vn.CarRentalWeb.repository.CarRepository;
import hcmute.edu.vn.CarRentalWeb.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    CarRepository carRepo;

    public int currentYear = LocalDate.now().getYear();
    public String status = "Đã hoàn thành";

    public List<Order> getAllOrder(){
        return orderRepo.findAll();
    }
    public int countOrderByEmail(String email){
        return orderRepo.countByAccountemail(email);
    }
    public int countOrdersByStatus(String status) {
        return orderRepo.countByStatus(status);
    }
    public List<Order> getAllOrderByEmail(String email){
        return orderRepo.findAllByAccountemail(email);
    }
    public int countCompletedOrdersThisMonth(){return orderRepo.countCompletedOrdersThisMonth();}
    public Map<Integer, BigDecimal> getMonthlyRevenue() {
        List<Object[]> results = orderRepo.findMonthlyRevenueByYearAndStatus(currentYear, status);
        Map<Integer, BigDecimal> monthlyRevenue = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            monthlyRevenue.put(i, BigDecimal.ZERO);
        }
        for (Object[] row : results) {
            Integer month = ((Number) row[0]).intValue();
            BigDecimal total = (BigDecimal) row[1];
            monthlyRevenue.put(month, total);
        }
        return monthlyRevenue;
    }
    public BigDecimal getRevenuethisYear() {return orderRepo.getYearlyRevenueByStatus();}
    public BigDecimal getRevenuethisMonth() {return orderRepo.getMonthlyRevenueByStatus();}
    public Order getOrderById(int id) {return orderRepo.findOrderById(id);}
    public List<Order> getRecentOrder() {
        List<Order> order = orderRepo.findAll();
        order.sort((o1, o2) -> o2.getCreatedat().compareTo(o1.getCreatedat()));
        return order.stream().limit(5).collect(Collectors.toList()); // chỉ lấy 5 đơn mới nhất
    }
    public void save(Order order ) {

        orderRepo.save(order);
    }

    @Transactional
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
            Car car = carRepo.findCarById(data.getCarid());
            car.setStatus("Đang thuê");
            orderRepo.save(order);
            carRepo.save(car);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public long calculateCountDate(Date startDate, Date endDate) {
        LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return ChronoUnit.DAYS.between(start, end) + 1;
    }

    public long calculateTotal(long countDate, int carPrice, Integer servicePrice) {
        long total = countDate * carPrice;
        if (servicePrice != null) {
            total += servicePrice;
        }
        return total;
    }


}

