package me.hkaibni.model.family;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "families")
public class Family {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "name_en", nullable = false, length = 100)
    private String nameEn;

    @Column(name = "name_ar", nullable = false, length = 100)
    private String nameAr;

    public Family() {
    }

    public Family(String nameEn, String nameAr) {
        this.nameEn = nameEn;
        this.nameAr = nameAr;
    }

    public UUID getId() {
        return id;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }
}