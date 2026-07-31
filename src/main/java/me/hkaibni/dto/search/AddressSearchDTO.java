package me.hkaibni.dto.search;

public class AddressSearchDTO extends SearchDTO {

    private String code;
    private String governorate;
    private String village;
    private Double latitude;
    private Double longitude;


    public boolean hasNoCriteria() {
        return isBlank(governorate) && isBlank(code)
                && isBlank(village)
                && isBlank(latitude)
                && isBlank(longitude);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
    private boolean isBlank(Double value) {
        return value == null;
    }

    public String getGovernorate() {
        return governorate;
    }

    public void setGovernorate(String governorate) {
        this.governorate = governorate;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}