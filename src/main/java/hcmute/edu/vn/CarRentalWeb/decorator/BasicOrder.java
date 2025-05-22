package hcmute.edu.vn.CarRentalWeb.decorator;

public class BasicOrder implements OrderComponent{

    private long countDate;
    private int carPrice;

    public BasicOrder(long countDate, int carPrice) {
        this.countDate = countDate;
        this.carPrice = carPrice;
    }

    @Override
    public long calculateTotal() {
        return countDate * carPrice;
    }
}
