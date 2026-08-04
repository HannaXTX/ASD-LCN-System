package me.hkaibni.dto.search;

public class UserSearchDTO extends SearchDTO {

    private String ssn;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String addressId;


    public boolean hasNoCriteria() {
        return isBlank(firstName)
                && isBlank(lastName)
                && isBlank(email)
                && isBlank(phone)
                && isBlank(addressId);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }



    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }
}