package com.badrul.awlacompany;

public class Company {


    private String companyID;
    private String companyName;
    private String companyEmail;
    private String companyDetails;
    private String companyLogo;



    public Company(String companyID, String companyName, String companyEmail, String companyDetails, String companyLogo) {

        this.companyID = companyID;
        this.companyName = companyName;
        this.companyEmail = companyEmail;
        this.companyDetails = companyDetails;
        this.companyLogo = companyLogo;


    }

    public String getCompanyID() {
        return companyID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public String getCompanyDetails() {
        return companyDetails;
    }


    public String getCompanyLogo() {
        return companyLogo;
    }

}
