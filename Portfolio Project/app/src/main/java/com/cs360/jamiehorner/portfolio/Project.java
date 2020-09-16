package com.cs360.jamiehorner.portfolio;

public class Project {
    // instance variables
    private int var_project_id;
    private int var_user_id;
    private String var_project_title;
    private String var_project_path;
    private String var_project_status;


    // empty constructor
    public Project() { }

    // constructor with all three variables
    public Project(
        int project_id,
        int user_id,
        String project_title,
        String project_path,
        String project_status)
    {
        this.var_project_id = project_id;
        this.var_user_id = user_id;
        this.var_project_title = project_title;
        this.var_project_path = project_path;
        this.var_project_status = project_status;

    }
    // constructor without id
    public Project(
        int user_id,
        String project_title,
        String project_path,
        String project_status )
    {
        this.var_user_id = user_id;
        this.var_project_title = project_title;
        this.var_project_path = project_path;
        this.var_project_status = project_status;
    }

    // setters (mutators)
    public void setProjectID(int project_id){ this.var_project_id = project_id; }
    public void setUserID(int user_id){ this.var_user_id = user_id; }
    public void setProject_title(String project_title){ this.var_project_title = project_title; }
    public void setProject_path(String project_path){ this.var_project_path = project_path; }
    public void setProject_status(String project_status){ this.var_project_status = project_status; }


    // getters (accessors)
    public int getProjectID(){ return this.var_project_id; }
    public int getUserID(){ return this.var_user_id; }
    public String getProject_title(){ return this.var_project_title; }
    public String getProject_path(){ return this.var_project_path; }
    public String getProject_staus(){ return this.var_project_status; }
}