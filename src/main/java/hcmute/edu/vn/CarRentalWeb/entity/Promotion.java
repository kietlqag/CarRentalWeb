package hcmute.edu.vn.CarRentalWeb.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "promotions")
public class Promotion {
    @Id
    public int id;
    public String code;
    public String description;
    public int discountpercent;
    public int type;
    public int isactive;

    public int getIsactive() {
        return isactive;
    }

    public void setIsactive(int isactive) {
        this.isactive = isactive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDiscountpercent() {
        return discountpercent;
    }

    public void setDiscountpercent(int discountpercent) {
        this.discountpercent = discountpercent;
    }



    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}