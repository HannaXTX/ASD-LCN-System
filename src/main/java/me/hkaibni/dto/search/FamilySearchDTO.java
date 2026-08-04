package me.hkaibni.dto.search;

public class FamilySearchDTO extends SearchDTO {

    String nameAr;
    String nameEn;

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public boolean hasNoCriteria() {
        return isBlank(nameAr)
                && isBlank(nameEn);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
