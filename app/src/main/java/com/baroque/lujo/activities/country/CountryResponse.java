package com.baroque.lujo.activities.country;

import java.util.List;

import com.baroque.lujo.activities.country.CountryModel;

public class CountryResponse {
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<CountryModel> getContent() {
        return content;
    }

    public void setContent(List<CountryModel> content) {
        this.content = content;
    }

    private Integer code;
    private String type;
    private List<CountryModel> content;
}
