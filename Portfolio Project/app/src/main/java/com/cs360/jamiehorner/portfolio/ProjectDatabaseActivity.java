package com.cs360.jamiehorner.portfolio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ProjectDatabaseActivity extends AppCompatActivity {
    TextView ProjectIdView;
    EditText UserIDBox;
    EditText ProjectTitleBox;
    EditText ProjectPathBox;
    EditText ProjectStatusBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_activity);
        ProjectIdView = (TextView) findViewById(R.id.ProjectID);
        UserIDBox = (EditText) findViewById(R.id.UserID);
        ProjectTitleBox = (EditText) findViewById(R.id.ProjectTitle);
        ProjectPathBox = (EditText) findViewById(R.id.ProjectPath);
        ProjectStatusBox = (EditText) findViewById(R.id.ProjectStatus);

    }
    public void addProject(View view) {
        ProjectDBHandler dbHandler = new ProjectDBHandler(this, null, null, 1);
        Project project = new Project(
            Integer.parseInt(UserIDBox.getText().toString()),
            ProjectTitleBox.getText().toString(),
            ProjectPathBox.getText().toString(),
            ProjectStatusBox.getText().toString()
        );

        dbHandler.addProject(project);
        UserIDBox.setText("");
        ProjectTitleBox.setText("");
        ProjectPathBox.setText("");
        ProjectStatusBox.setText("");

    }
    public void searchProject(View view) {
        ProjectDBHandler dbHandler = new ProjectDBHandler(this, null, null, 1);
        Project project = dbHandler.searchProject(ProjectTitleBox.getText().toString());
        if (project != null) {
            ProjectIdView.setText(String.valueOf(project.getProjectID()));
            UserIDBox.setText(String.valueOf(project.getUserID()));
            ProjectTitleBox.setText(String.valueOf(project.getProject_title()));
            ProjectPathBox.setText(String.valueOf(project.getProject_path()));
            ProjectStatusBox.setText(String.valueOf(project.getProject_staus()));
        } else {
            ProjectIdView.setText("User not found.");
        }
    }
    public void deleteUser(View view) {
        UserDBHandler dbHandler = new UserDBHandler(this, null, null, 1);
        boolean result = dbHandler.deleteUser(ProjectTitleBox.getText().toString());
        if (result)
        {
            ProjectIdView.setText("Project ID Deleted");
            UserIDBox.setText("User ID Deleted");
            ProjectTitleBox.setText("Project Title Deleted");
            ProjectPathBox.setText("Project Path Deleted");
            ProjectStatusBox.setText("Project Status Deleted");
        }
        else {
            ProjectIdView.setText("User not found.");
        }
    }
    public void SwitchToUserDB(View view) {
        Intent intent = new Intent(this, DatabaseActivity.class);
        startActivity(intent);

    }
}
