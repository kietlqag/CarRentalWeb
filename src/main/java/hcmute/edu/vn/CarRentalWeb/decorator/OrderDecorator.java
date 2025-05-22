package hcmute.edu.vn.CarRentalWeb.decorator;


public abstract class OrderDecorator implements OrderComponent{

    protected OrderComponent decoratedOrder;

    public OrderDecorator(OrderComponent decoratedOrder) {
        this.decoratedOrder = decoratedOrder;
    }

    @Override
    public long calculateTotal() {
        return decoratedOrder.calculateTotal();
    }
}
