package hcmute.edu.vn.CarRentalWeb.dto;

public class AccountUpdateRequest {
    private String email;
    private String role;

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
