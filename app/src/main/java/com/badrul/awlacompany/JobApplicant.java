package com.badrul.awlacompany;

public class JobApplicant {


    private String userID;
    private String userName;
    private String userEmail;
    private String userPhone;
    private String userAge;
    private String userWorkExp;
    private String userToken;
    private String iv_url;
    private String applyStatus;

    public JobApplicant(String userID, String userName, String userEmail, String userPhone, String userAge,String userWorkExp,String userToken,String iv_url,String applyStatus) {

        this.userID = userID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userAge = userAge;
        this.userWorkExp = userWorkExp;
        this.userToken = userToken;
        this.iv_url = iv_url;
        this.applyStatus = applyStatus;


    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }


    public String getUserAge() {
        return userAge;
    }

    public String getUserWorkExp() {
        return userWorkExp;
    }
    public String getUserToken() {
        return userToken;
    }
    public String getIv_url() {
        return iv_url;
    }
    public String getApplyStatus() {
        return applyStatus;
    }
}
