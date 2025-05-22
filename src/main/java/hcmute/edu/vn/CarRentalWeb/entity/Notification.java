package hcmute.edu.vn.CarRentalWeb.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String accountemail;
    private String title;
    private String message;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdat = new Date();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountemail() {
        return accountemail;
    }

    public void setAccountemail(String accountemail) {
        this.accountemail = accountemail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Date createdat) {
        this.createdat = createdat;
    }
}
