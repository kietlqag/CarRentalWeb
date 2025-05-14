package hcmute.edu.vn.CarRentalWeb.dto;

import java.math.BigDecimal;

public class CheckoutRequest {
    // Thông tin khách hàng
    private String customer;
    private String email;
    private String phone;
    private String picklocation;
    private String note;

    // Dữ liệu tóm tắt đơn hàng
    private String name;
    private String service;
    private String receiveDate;   // hoặc dùng LocalDate nếu bạn chuyển kiểu
    private String returnDate;
    private int countDate;
    private BigDecimal total;

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPicklocation() {
        return picklocation;
    }

    public void setPicklocation(String picklocation) {
        this.picklocation = picklocation;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public int getCountDate() {
        return countDate;
    }

    public void setCountDate(int countDate) {
        this.countDate = countDate;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
