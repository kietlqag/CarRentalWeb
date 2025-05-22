package hcmute.edu.vn.CarRentalWeb.decorator;

public class ServiceDecorator extends OrderDecorator {
    private Integer servicePrice;

    public ServiceDecorator(OrderComponent decoratedOrder, Integer servicePrice) {
        super(decoratedOrder);
        this.servicePrice = servicePrice;
    }

    @Override
    public long calculateTotal() {
        long baseTotal = super.calculateTotal();
        if (servicePrice != null) {
            return baseTotal + servicePrice;
        }
        return baseTotal;
    }
}

