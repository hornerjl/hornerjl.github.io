package com.cs360.jamiehorner.portfolio;

public class User {
    // instance variables
    private int var_user_id;
    private String var_email_address;
    private String var_first_name;
    private String var_last_name;
    private String var_middle_initial;
    private String var_job_title;
    private String var_phone_number;
    private String var_freelancer_address;
    private String var_twitter_address;
    private String var_facebook_address;
    private String var_linkedin_address;
    private String var_resume_import_path;
    private String var_user_image_path;

    // empty constructor
    public User() {
    }

    // constructor with all three variables
    public User(
        int user_id,
        String phone_number,
        String email_address,
        String first_name,
        String last_name,
        String middle_initial,
        String job_title,
        String freelancer_address,
        String twitter_address,
        String facebook_address,
        String linkedin_address,
        String resume_import_path,
        String user_image_path) {
        this.var_user_id = user_id;
        this.var_email_address = email_address;
        this.var_first_name = first_name;
        this.var_last_name = last_name;
        this.var_middle_initial = middle_initial;
        this.var_job_title = job_title;
        this.var_phone_number = phone_number;
        this.var_freelancer_address = freelancer_address;
        this.var_twitter_address = twitter_address;
        this.var_facebook_address = facebook_address;
        this.var_linkedin_address = linkedin_address;
        this.var_resume_import_path = resume_import_path;
        this.var_user_image_path = user_image_path;
    }

    // constructor without id
    public User(
        String phone_number,
        String email_address,
        String first_name,
        String last_name,
        String middle_initial,
        String job_title,
        String freelancer_address,
        String twitter_address,
        String facebook_address,
        String linkedin_address,
        String resume_import_path,
        String user_image_path)

    {
        this.var_email_address = email_address;
        this.var_first_name = first_name;
        this.var_last_name = last_name;
        this.var_middle_initial = middle_initial;
        this.var_job_title = job_title;
        this.var_phone_number = phone_number;
        this.var_freelancer_address = freelancer_address;
        this.var_twitter_address = twitter_address;
        this.var_facebook_address = facebook_address;
        this.var_linkedin_address = linkedin_address;
        this.var_resume_import_path = resume_import_path;
        this.var_user_image_path = user_image_path;
    }

    // setters (mutators)
    public void setUserID(int user_id){ this.var_user_id = user_id; }
    public void setEmailAddress(String email_address){ this.var_email_address = email_address; }
    public void setFirstName(String first_name){ this.var_first_name = first_name; }
    public void setLastName(String last_name){ this.var_last_name = last_name; }
    public void setMiddleInitial(String middle_initial){ this.var_middle_initial = middle_initial; }
    public void setJobTitle(String job_title){ this.var_job_title = job_title; }
    public void setPhoneNumber(String phone_number){ this.var_phone_number = phone_number; }
    public void setFreelancerAddress(String freelancer_address){ this.var_freelancer_address = freelancer_address; }
    public void setFacebookAddress(String twitter_address){ this.var_twitter_address = twitter_address; }
    public void setTwitterAddress(String facebook_address){ this.var_facebook_address = facebook_address; }
    public void setLinkedinAddress(String linkedin_address){ this.var_linkedin_address = linkedin_address; }
    public void setResumeImportPath(String resume_import_path){ this.var_resume_import_path = resume_import_path; }
    public void setUserImagePath(String user_image_path){ this.var_user_image_path = user_image_path; }

    // getters (accessors)
    public int getUserID(){ return this.var_user_id; }
    public String getEmailAddress(){ return this.var_email_address; }
    public String getFirstName(){ return this.var_first_name; }
    public String getLastName(){ return this.var_last_name; }
    public String getMiddleInitial(){ return this.var_middle_initial; }
    public String getJobTitle(){ return this.var_job_title; }
    public String getPhoneNumber(){ return this.var_phone_number ; }
    public String getFreelancerAddress(){ return this.var_freelancer_address; }
    public String getFacebookAddress(){ return this.var_twitter_address; }
    public String getTwitterAddress(){ return this.var_facebook_address; }
    public String getLinkedinAddress(){ return this.var_linkedin_address; }
    public String getResumeImportPath(){ return this.var_resume_import_path; }
    public String getUserImagePath(){ return this.var_user_image_path; }
}
