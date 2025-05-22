package hcmute.edu.vn.CarRentalWeb.service;

import hcmute.edu.vn.CarRentalWeb.strategy.PaymentStrategy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentContextService {
    private Map<String, PaymentStrategy> strategyMap;

    public PaymentContextService(List<PaymentStrategy> strategies) {
        strategyMap = new HashMap<>();
        for (PaymentStrategy strategy : strategies) {
            strategyMap.put(strategy.getName().toUpperCase(), strategy);
        }
    }

    public PaymentStrategy getPaymentStrategy(String method) {
        if (method == null) {
            throw new IllegalArgumentException("Phương thức thanh toán không được null");
        }
        PaymentStrategy strategy = strategyMap.get(method.toUpperCase());
        if (strategy == null) {
            throw new IllegalArgumentException("Phương thức thanh toán không hợp lệ: " + method);
        }
        return strategy;
    }
}

