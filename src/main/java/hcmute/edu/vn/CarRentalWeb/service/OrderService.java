package hcmute.edu.vn.CarRentalWeb.service;

import hcmute.edu.vn.CarRentalWeb.decorator.BasicOrder;
import hcmute.edu.vn.CarRentalWeb.decorator.OrderComponent;
import hcmute.edu.vn.CarRentalWeb.decorator.ServiceDecorator;
import hcmute.edu.vn.CarRentalWeb.dto.CheckoutRequest;
import hcmute.edu.vn.CarRentalWeb.entity.Car;
import hcmute.edu.vn.CarRentalWeb.entity.Order;
import hcmute.edu.vn.CarRentalWeb.observer.*;
import hcmute.edu.vn.CarRentalWeb.repository.CarRepository;
import hcmute.edu.vn.CarRentalWeb.repository.OrderRepository;
import hcmute.edu.vn.CarRentalWeb.strategy.PaymentStrategy;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    CarRepository carRepo;

    //Observer
    @Autowired
    private OrderSubject orderSubject;
    @Autowired
    private CarStatusUpdater carStatusUpdater;
    @Autowired
    private StaffNotifier staffNotifier;
    @Autowired
    private CustomerNotifier customerNotifier;
    @Autowired
    private OrderStatusSubject orderStatusSubject;
    @Autowired
    private OrderStatusNotifier orderStatusNotifier;
    @Autowired
    private OrderPaymentStatusSubject orderPaymentStatusSubject;
    @Autowired
    private OrderPaymentStatusNotifier orderPaymentStatusNotifier;


    @PostConstruct
    public void initObservers() {
        orderSubject.register(carStatusUpdater);
        orderSubject.register(staffNotifier);
        orderSubject.register(customerNotifier);
        orderStatusSubject.register(orderStatusNotifier);
        orderPaymentStatusSubject.register(orderPaymentStatusNotifier);
    }

    //strategy
    private final PaymentContextService paymentContext;

    public OrderService(PaymentContextService paymentContext) {
        this.paymentContext = paymentContext;
    }

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
        return orderRepo.findAllByAccountemailOrderByCreatedatDesc(email);
    }
    public int countCompletedOrdersThisYear(){return orderRepo.countCompletedOrdersThisYear();}
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
    public Order getOrderById(int id) {return orderRepo.findOrderById(id);}
    public List<Order> getRecentOrder() {
        return orderRepo.findTop5ByOrderByCreatedatDesc();
}
    public void save(Order order ) {

        orderRepo.save(order);
        orderStatusSubject.notifyAll(order);
        orderPaymentStatusSubject.notifyAll(order);
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
            order.setCreatedat(LocalDateTime.now());
            order.setName(data.getName());
            order.setService(data.getService() != null ? data.getService() : "Không");
            order.setReceivedate(data.getReceiveDate());
            order.setReturndate(data.getReturnDate());
            order.setCountdate(data.getCountDate());
            order.setTotal(data.getTotal());
            order.setDiscount(data.getDiscount());
            order.setPrice(data.getPrice());
            order.setServiceid(data.getServiceid());
            order.setCarid(data.getCarid());
            order.setServiceprice(data.getServiceprice());

            PaymentStrategy paymentStrategy = paymentContext.getPaymentStrategy(data.getPaymentmethod());
            paymentStrategy.pay(order);

            orderRepo.save(order);
            orderSubject.notifyAll(order);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int calculateCountDate(Date receivedDate, Date returnDate) {
        Date utilReceived = new Date(receivedDate.getTime());
        Date utilReturn = new Date(returnDate.getTime());

        LocalDate start = utilReceived.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate end = utilReturn.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return (int) ChronoUnit.DAYS.between(start, end) + 1;
    }



    public long calculateTotal(long countDate, int carPrice, Integer servicePrice) {
        OrderComponent basicOrder = new BasicOrder(countDate, carPrice);
        if (servicePrice != null && servicePrice > 0) {
            basicOrder = new ServiceDecorator(basicOrder, servicePrice);
        }
        return basicOrder.calculateTotal();
    }


    public BigDecimal updateTotal(long countDate, int price, int priceService, int discount) {
        BigDecimal dailyPrice = BigDecimal.valueOf(price);
        BigDecimal servicePrice = BigDecimal.valueOf(priceService);
        BigDecimal discountPercent = BigDecimal.valueOf(discount).divide(BigDecimal.valueOf(100));

        BigDecimal rentalCost = dailyPrice.multiply(BigDecimal.valueOf(countDate));
        BigDecimal subtotal = rentalCost.add(servicePrice);
        BigDecimal discountAmount = subtotal.multiply(discountPercent);
        BigDecimal total = subtotal.subtract(discountAmount);

        return total;
    }

    @Transactional
    public boolean updateStatus(int orderid, int carid) {
        try {
            Order order = orderRepo.findOrderById(orderid);
            Car car = carRepo.findCarById(carid);

            order.setStatus("Đã huỷ");
            car.setStatus("Sẵn sàng");

            orderRepo.save(order);
            carRepo.save(car);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public boolean updateCarStatusForStaff(int orderid, int carid) {
        try {
            Order order = orderRepo.findOrderById(orderid);
            Car car = carRepo.findCarById(carid);

            order.setStatus("Đã huỷ");
            car.setStatus("Sẵn sàng");

            orderRepo.save(order);
            carRepo.save(car);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Order> getOrdersByEmail(String email) {
        List<Order> orderList = orderRepo.findAllByAccountemailOrderByCreatedatDesc(email);
        return orderList.stream().limit(5).collect(Collectors.toList());
    }

}

