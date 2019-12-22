package com.badrul.awlacompany;

public class Job {


    private String jobID;
    private String jobPosition;
    private String jobDetails;
    private String jobOpenDate;
    private String jobCloseDate;
    private String jobCategory;
    private String jobStatus;


    public Job(String jobID, String jobPosition, String jobDetails, String jobOpenDate, String jobCloseDate,String jobCategory,String jobStatus) {

        this.jobID = jobID;
        this.jobPosition = jobPosition;
        this.jobDetails = jobDetails;
        this.jobOpenDate = jobOpenDate;
        this.jobCloseDate = jobCloseDate;
        this.jobCategory = jobCategory;
        this.jobStatus = jobStatus;


    }

    public String getJobID() {
        return jobID;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public String getJobDetails() {
        return jobDetails;
    }

    public String getJobOpenDate() {
        return jobOpenDate;
    }

    public String getJobCloseDate() {
        return jobCloseDate;
    }

    public String getJobCategory() {
        return jobCategory;
    }

    public String getJobStatus() {
        return jobStatus;
    }
}
