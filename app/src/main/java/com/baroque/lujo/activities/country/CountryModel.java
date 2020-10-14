package com.baroque.lujo.activities.country;

import java.io.Serializable;

public class CountryModel implements Serializable {
    private Integer id;
    private String alpha2Code;
    private String phonePrefix;
    private String nationality;
    private String country;
    private String image;

    public CountryModel(Integer id, String alpha2Code, String phonePrefix, String nationality, String country, String image) {
        this.id = id;
        this.alpha2Code = alpha2Code;
        this.phonePrefix = phonePrefix;
        this.nationality = nationality;
        this.country = country;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAlpha2Code() {
        return alpha2Code;
    }

    public void setAlpha2Code(String alpha2Code) {
        this.alpha2Code = alpha2Code;
    }

    public String getPhonePrefix() {
        return phonePrefix;
    }

    public void setPhonePrefix(String phonePrefix) {
        this.phonePrefix = phonePrefix;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
