package com.mycompany.java_ml_webapp;

import java.util.List;

/**
 *
 * @author Omar Safwat
 */
class Job {
    
    String title;
    String Company;
    String location;
    String type;
    String yearsExp;
    String country;
    List<String> skills;

    public Job(String title, String Company, String location, String type, String yearsExp, String country, List<String> skills) {
        this.title = title;
        this.Company = Company;
        this.location = location;
        this.type = type;
        this.yearsExp = yearsExp;
        this.country = country;
        this.skills = skills;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String Company) {
        this.Company = Company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getYearsExp() {
        return yearsExp;
    }

    public void setYearsExp(String yearsExp) {
        this.yearsExp = yearsExp;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }


}
