package hcmute.edu.vn.CarRentalWeb.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

public class CheckoutRequest {

    private String customer;
    private String email;
    private String phone;
    private String picklocation;
    private String note;
    private String paymentmethod;

    private String name;
    private String service;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date receiveDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date returnDate;
    private int countDate;
    private BigDecimal total;
    private int price;
    private int discount;
    private int carid;
    private int serviceid;
    private int serviceprice;

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

    public String getPaymentmethod() {
        return paymentmethod;
    }

    public void setPaymentmethod(String paymentmethod) {
        this.paymentmethod = paymentmethod;
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

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getCarid() {
        return carid;
    }

    public void setCarid(int carid) {
        this.carid = carid;
    }

    public int getServiceid() {
        return serviceid;
    }

    public void setServiceid(int serviceid) {
        this.serviceid = serviceid;
    }

    public int getServiceprice() {
        return serviceprice;
    }

    public void setServiceprice(int serviceprice) {
        this.serviceprice = serviceprice;
    }
}
