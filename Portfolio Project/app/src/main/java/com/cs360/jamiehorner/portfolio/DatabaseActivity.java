package com.cs360.jamiehorner.portfolio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DatabaseActivity extends AppCompatActivity {
    //Variables for view objects in activity_main.xml
    TextView userIdView;
    EditText usernameBox;
    EditText passwordBox;
    EditText emailAddressBox;
    EditText firstNameBox;
    EditText lastNameBox;
    EditText middleInitialBox;
    EditText jobTitleBox;
    EditText phoneNumberBox;
    EditText freelancerAddressBox;
    EditText twitterAddressBox;
    EditText facebookAddressBox;
    EditText linkedinBox;
    EditText resumeImportPathBox;
    EditText userImagePathBox;
    EditText MapLocationBox;
    LinearLayout LoginLayout;
    TableLayout NewUserTable;
    LinearLayout LoggedInButtons;
    LinearLayout NewUserButtons;
    TextView UsernameDisplay;

    //Add a handler allowing linkedIn API thread to communicate with the main thread
    Handler mainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        //When successful message is recieved pass it along to handling function
        public void handleMessage(Message inputMessage) {
            setUsernameToLinkedinName(inputMessage);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Link view variables by id to the actual view objects
        setContentView(R.layout.activity_main);
        userIdView = (TextView) findViewById(R.id.ProjectID);
        usernameBox = (EditText) findViewById(R.id.UserID);
        passwordBox = (EditText) findViewById(R.id.Password);
        emailAddressBox = (EditText) findViewById(R.id.Email);
        firstNameBox = (EditText) findViewById(R.id.FirstName);
        lastNameBox = (EditText) findViewById(R.id.LastName);
        middleInitialBox = (EditText) findViewById(R.id.MiddleInitial);
        jobTitleBox = (EditText) findViewById(R.id.JobTitle);
        phoneNumberBox = (EditText) findViewById(R.id.PhoneNumber);
        freelancerAddressBox = (EditText) findViewById(R.id.FreelancerAddress);
        twitterAddressBox = (EditText) findViewById(R.id.TwitterAddress);
        facebookAddressBox = (EditText) findViewById(R.id.FacebookAddress);
        linkedinBox = (EditText) findViewById(R.id.LinkedInAddress);
        resumeImportPathBox = (EditText) findViewById(R.id.ResumeImportPath);
        userImagePathBox = (EditText) findViewById(R.id.UserImagePath);
        MapLocationBox = (EditText) findViewById(R.id.MapLocation);
        LoginLayout = (LinearLayout) findViewById(R.id.LoginLayout);
        NewUserTable = (TableLayout) findViewById(R.id.NewUserTable);
        LoggedInButtons = (LinearLayout) findViewById(R.id.LoggedInButtons);
        NewUserButtons = (LinearLayout) findViewById(R.id.NewUserButtons);
        UsernameDisplay = (TextView) findViewById(R.id.UsernameDisplay);

        //If app was opened from another app(the browser), call linkedin handler function
        Intent appLinkIntent = getIntent();
        Uri appLinkData = appLinkIntent.getData();
        if (appLinkData != null) {
            GetLinkedInUsername(appLinkData);
        }

        //Set everything to invisible except for the initial login
        NewUserTable.setVisibility(View.GONE);
        NewUserButtons.setVisibility(View.GONE);
        LoggedInButtons.setVisibility(View.GONE);
    }
    public void addUser(View view) {
        //Create user and user database object
        UserDBHandler dbHandler = new UserDBHandler(this, null, null, 1);
        User user = new User(phoneNumberBox.getText().toString(),
            emailAddressBox.getText().toString(),
            firstNameBox.getText().toString(),
            lastNameBox.getText().toString(),
            middleInitialBox.getText().toString(),
            jobTitleBox.getText().toString(),
            freelancerAddressBox.getText().toString(),
            twitterAddressBox.getText().toString(),
            facebookAddressBox.getText().toString(),
            linkedinBox.getText().toString(),
            resumeImportPathBox.getText().toString(),
            userImagePathBox.getText().toString()
        );

        //Add generated user object to SQL database
        dbHandler.addUser(user);

        //Set all user input boxes to empty
        emailAddressBox.setText("");
        firstNameBox.setText("");
        lastNameBox.setText("");
        middleInitialBox.setText("");
        jobTitleBox.setText("");
        phoneNumberBox.setText("");
        freelancerAddressBox.setText("");
        twitterAddressBox.setText("");
        facebookAddressBox.setText("");
        linkedinBox.setText("");
        resumeImportPathBox.setText("");
        userImagePathBox.setText("");
    }
    public void searchUser(View view) {
        //Create a User database object
        UserDBHandler dbHandler = new UserDBHandler(this, null, null, 1);

        //Find a user with the text in the email textbox and if not null set all text boxes to the values found
        User user = dbHandler.searchUser(emailAddressBox.getText().toString());
        if (user != null) {
            userIdView.setText(String.valueOf(user.getUserID()));
            emailAddressBox.setText(String.valueOf(user.getEmailAddress()));
            firstNameBox.setText(String.valueOf(user.getFirstName()));
            lastNameBox.setText(String.valueOf(user.getLastName()));
            middleInitialBox.setText(String.valueOf(user.getMiddleInitial()));
            jobTitleBox.setText(String.valueOf(user.getJobTitle()));
            phoneNumberBox.setText(String.valueOf(user.getPhoneNumber()));
            freelancerAddressBox.setText(String.valueOf(user.getFreelancerAddress()));
            twitterAddressBox.setText(String.valueOf(user.getTwitterAddress()));
            facebookAddressBox.setText(String.valueOf(user.getFacebookAddress()));
            linkedinBox.setText(String.valueOf(user.getLinkedinAddress()));
            resumeImportPathBox.setText(String.valueOf(user.getResumeImportPath()));
            userImagePathBox.setText(String.valueOf(user.getUserImagePath()));
        } else {
            userIdView.setText("User not found.");
        }
    }
    public void deleteUser(View view) {
        //Create a User database object
        UserDBHandler dbHandler = new UserDBHandler(this, null, null, 1);

        /*Delete user found by email textbox text and
        based on whether that user was found set everything to show deletion*/
        boolean result = dbHandler.deleteUser(emailAddressBox.getText().toString());
        if (result)
        {
            userIdView.setText("User ID Deleted");
            emailAddressBox.setText("Email Address Deleted");
            firstNameBox.setText("First Name Deleted");
            lastNameBox.setText("Last Name Deleted");
            middleInitialBox.setText("Middle Initial Deleted");
            jobTitleBox.setText("Job Title Deleted");
            phoneNumberBox.setText("Phone Number Deleted");
            freelancerAddressBox.setText("Freelancer Address Deleted");
            twitterAddressBox.setText("Twitter Address Deleted");
            facebookAddressBox.setText("Facebook Address Deleted");
            linkedinBox.setText("Linkedin Deleted");
            resumeImportPathBox.setText("Resume Deleted");
            userImagePathBox.setText("User Image Deleted");
        }
        else {
            userIdView.setText("User not found.");
        }
    }

    public void storeLogin(View view) {
        //Get app's shared preferences and store username and password textbox text
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", usernameBox.getText().toString());
        editor.putString("password", passwordBox.getText().toString());
        editor.apply();

        //Set textbox text to empty
        usernameBox.setText("");
        passwordBox.setText("");
    }

    public void getLogin(View view) {
        //Get app's shared preferences values for username/password and currently entered values
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String enteredUsername = usernameBox.getText().toString();
        String enteredPassword = passwordBox.getText().toString();
        String username = sharedPref.getString("username", "n/a");
        String password = sharedPref.getString("password", "n/a");

        //If there is no username/password stored in preferences notify user
        if (username.equals("n/a") || password.equals("n/a")) {
            usernameBox.setText("No login data found");
            passwordBox.setText("No login data found");
            return;
        }

        /*If entered username/password equals stored username/password set login view to invisible
        and user database view to visible*/
        if (username.equals(enteredUsername) && password.equals(enteredPassword)) {
            UsernameDisplay.setText(username);
            LoginLayout.setVisibility(View.GONE);
            NewUserTable.setVisibility(View.VISIBLE);
            NewUserButtons.setVisibility(View.VISIBLE);
            LoggedInButtons.setVisibility(View.VISIBLE);
            return;
        }

        //If entered username/password does not equal stored username/password notify user
        usernameBox.setText("Invalid login");
        passwordBox.setText("Invalid login");
        return;
    }

    //Switch Activity to Project Database Activity
    public void SwitchToProjectDB(View view) {
        Intent intent = new Intent(this, ProjectDatabaseActivity.class);
        startActivity(intent);
    }

    //Switch Activity to Google Maps Activity passing the value of the location textbox
    public void SwitchToGoogleMaps(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("MapLocation", MapLocationBox.getText().toString());
        startActivity(intent);
    }

    //Switch Activity to Photo Gallery Activity
    public void SwitchToPhotoGallery(View view) {
        Intent intent = new Intent(this, PhotoGalleryActivity.class);
        startActivity(intent);
    }

    public void LoginWithLinkedIn(View view) {
        //Create an intent to pass along to the browser with a Linkedin signin URL
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/oauth/v2/authorization" +
                "?response_type=code" +
                "&client_id=78yhldao51f5qo" +
                "&redirect_uri=https://portfolio.redirect/linkedin" +
                "&state=validityAssured" +
                "&scope=r_liteprofile%20r_emailaddress%20w_member_social"));
        startActivity(browserIntent);
    }

    public void GetLinkedInUsername(Uri uri) {
        //Get the query parameters from the linkedin redirect URL
        String code = uri.getQueryParameter("code");
        String state = uri.getQueryParameter("state");

        //For Security check our state value that we sent out via our original browser intent
        if(state.equals("validityAssured")) {

            //Create an http request to get an access token from linkedin
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                .url("https://www.linkedin.com/oauth/v2/accessToken" +
                    "?grant_type=authorization_code" +
                    "&code=" + code +
                    "&redirect_uri=https%3A%2F%2Fportfolio.redirect%2Flinkedin" +
                    "&client_id=78yhldao51f5qo" +
                    "&client_secret=WdnDwvC9QJCoy9qH"
                )
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

            //Create a task to run in a separate thread for the http request
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    //Try to make the linkedin API call
                    try(Response response = client.newCall(request).execute()) {

                        //If not successful throw error for catch statement
                        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                        //Parse successful response as a JSON object
                        JSONObject parsedResponse = new JSONObject(response.body().string());

                        //Build new request using parsed access token from the previous response
                        Request usernameRequest =new Request.Builder()
                            .url("https://api.linkedin.com/v2/me")
                            .header(
                                "Authorization",
                                "Bearer " +
                                    parsedResponse.getString("access_token")
                            )
                            .get()
                            .build();

                        //Try to make the call to get information from linkedin using the auth token
                        try(Response usernameResponse = client.newCall(usernameRequest).execute())  {

                            //If not successful throw error for catch statement
                            if (!usernameResponse.isSuccessful()) throw new IOException("Unexpected code " + response);

                            //Parse successful response as a JSON object
                            JSONObject parsedUserInfo = new JSONObject(usernameResponse.body().string());

                            //create a new username string using the first and last name returned from linkedin response
                            String username = parsedUserInfo.getString("localizedFirstName") +
                                parsedUserInfo.getString("localizedLastName");

                            //construct basic message to the main thread where we can change the view object with the username
                            Message completeMessage = mainHandler.obtainMessage(0, username);
                            completeMessage.sendToTarget();
                        } catch (Exception exception) {
                            //Log any raised exceptions
                            Log.e("Linkedin", exception.toString());
                        }
                    } catch (Exception exception) {
                        //Log any raised exceptions
                        Log.e("Linkedin", exception.toString());
                    }
                }
            };

            //Start another thread for the task runnable which can perform http calls
            new Thread(task).start();
        }
    }

    public void setUsernameToLinkedinName (Message message) {
        //When linkedin login successful, set username to the passed in linkedin name
        UsernameDisplay.setText(message.obj.toString());
        //Swap visibility for login and database layouts
        LoginLayout.setVisibility(View.GONE);
        NewUserTable.setVisibility(View.VISIBLE);
        NewUserButtons.setVisibility(View.VISIBLE);
        LoggedInButtons.setVisibility(View.VISIBLE);
    }
}

