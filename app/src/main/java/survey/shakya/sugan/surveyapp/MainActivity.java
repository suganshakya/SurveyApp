package survey.shakya.sugan.surveyapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import survey.shakya.sugan.surveyapp.activity.ListSurveyActivity;
import survey.shakya.sugan.surveyapp.activity.ListSurveyActivityForSurveyee;
import survey.shakya.sugan.surveyapp.data.DataHelper;
import survey.shakya.sugan.surveyapp.model.User;

public class MainActivity extends AppCompatActivity {
    private static String TAG = MainActivity.class.getName();
    Context context = null;

    EditText usernameET, passwordET;    // For Sign IN

    EditText firstNameET, lastNameET;  // For Register
    EditText usernameET1, passwordET1, passwordET2;    // For Register
    RadioGroup userTypeRG;
    RadioButton surveyerRB, surveyeeRB;

    Button signInButton, registerButton;

    String username, password;
    String firstName, lastName;
    String username1, password1, password2;
    User.UserType userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        findViewById();
        context = getApplicationContext();
    }

    private void findViewById() {
        usernameET = (EditText) findViewById(R.id.edit_text_main_username);
        passwordET = (EditText) findViewById(R.id.edit_text_main_password);
        signInButton = (Button) findViewById(R.id.sign_in_button);

        firstNameET = (EditText) findViewById(R.id.edit_text_first_name);
        lastNameET = (EditText) findViewById(R.id.edit_text_last_name);
        usernameET1 = (EditText) findViewById(R.id.edit_text_username1);
        passwordET1 = (EditText) findViewById(R.id.edit_text_password1);
        passwordET2 = (EditText) findViewById(R.id.edit_text_password2);

        userTypeRG = (RadioGroup) findViewById(R.id.radio_group_user_role);
        surveyeeRB = (RadioButton) findViewById(R.id.radio_button_surveyee);
        surveyerRB = (RadioButton) findViewById(R.id.radio_button_surveyer);

        registerButton = (Button) findViewById(R.id.register_button);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void signIn(View view) {
        username = usernameET.getText().toString();
        if(username.contentEquals("") || username == null){
            Toast.makeText(getApplicationContext(), "Enter username.", Toast.LENGTH_SHORT).show();
            return;
        }
        password = passwordET.getText().toString();
        if(password.contentEquals("") || password == null){
            Toast.makeText(getApplicationContext(), "Enter password.", Toast.LENGTH_SHORT).show();
            return;
        }
        DataHelper dataHelper = DataHelper.getInstance(getApplicationContext());
        User user = dataHelper.getUser(username);   // return null if not exists
        if (user == null) {
            Toast.makeText(getApplicationContext(), "User: " + username + " not found in Database", Toast.LENGTH_SHORT).show();
        } else if (user.getPassword().contentEquals(password)) {
            Log.i(TAG, "Login Success");
            Intent intent = new Intent(MainActivity.this, ListSurveyActivity.class);
            intent.putExtra("USER_ID", user.getId());
            MainActivity.this.startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Password did not match", Toast.LENGTH_SHORT).show();
        }
    }

    public void register(View view) {
        DataHelper dataHelper = DataHelper.getInstance(getApplicationContext());
        username1 = usernameET1.getText().toString();
        User user = dataHelper.getUser(username1);
        if (user != null) {
            Toast.makeText(context, "User '" + username1 + "' already exists!", Toast.LENGTH_SHORT).show();
            return;
        }

        firstName = firstNameET.getText().toString();
        if(firstName.contentEquals("") || firstName == null){
            Toast.makeText(context, "Enter first Name.", Toast.LENGTH_SHORT).show();
            return;
        }
        lastName = lastNameET.getText().toString();

        if(username1 == null || username1.length() < 5){
            Toast.makeText(context, "Enter Username (Must be at least 5 characters long).", Toast.LENGTH_SHORT).show();
            return;
        }

        password1 = passwordET1.getText().toString();
        if(password1 == null || password1.length() < 5){
            Toast.makeText(context, "Enter Password (Must be at least 5 characters long).", Toast.LENGTH_SHORT).show();
            return;
        }
        password2 = passwordET2.getText().toString();
        if(! password1.contentEquals(password2)){
            Toast.makeText(context, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedRadioButtonId = userTypeRG.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = (RadioButton) userTypeRG.findViewById(selectedRadioButtonId);
        userType = User.UserType.valueOf(selectedRadioButton.getText().toString().toUpperCase());

        user = new User(firstName, lastName, username1, password1, userType);
        dataHelper.insertUser(user);
        Toast.makeText(getApplicationContext(), "Register Successful for User - " + username1 + " as " + userType.name(), Toast.LENGTH_LONG).show();
    }
}
