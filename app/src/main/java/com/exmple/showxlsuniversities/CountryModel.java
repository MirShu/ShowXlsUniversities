package com.exmple.showxlsuniversities;

public class CountryModel {
    private String Id;
    private String CityName;
    private String Code;
    private String AdminId;

    public String getAdminId() {
        return AdminId;
    }

    public void setAdminId(String adminId) {
        AdminId = adminId;
    }

    public String getId() {
        return this.Id;
    }

    public String getCityName() {
        return this.CityName;
    }

    public String getCode() {
        return this.Code;
    }

    public void setId(String paramString) {
        this.Id = paramString;
    }

    public void setCityName(String paramString) {
        this.CityName = paramString;
    }

    public void setCode(String paramString) {
        this.Code = paramString;
    }
}
