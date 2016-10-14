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
import android.widget.Toast;

import survey.shakya.sugan.surveyapp.activity.ListQuestionForSurveyerActivity;
import survey.shakya.sugan.surveyapp.activity.ListSurveyActivity;
import survey.shakya.sugan.surveyapp.data.DataHelper;
import survey.shakya.sugan.surveyapp.model.Surveyer;

public class MainActivity extends AppCompatActivity {
    private static String TAG = MainActivity.class.getName();
    Context context = null;

    EditText usernameET, passwordET, firstNameET, lastNameET;
    Button signInButton, registerButton, fillUpButton;

    String username, password;
    String firstName, lastName;

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

        firstNameET = (EditText) findViewById(R.id.edit_text_first_name_surveyee);
        lastNameET = (EditText) findViewById(R.id.edit_text_last_name_surveyee);

        signInButton = (Button) findViewById(R.id.sign_in_button);
        registerButton = (Button) findViewById(R.id.register_button);
        fillUpButton = (Button) findViewById(R.id.fill_up_button);
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

    public void register(View view) {
        username = usernameET.getText().toString();
        password = passwordET.getText().toString();
        DataHelper dataHelper = DataHelper.getInstance(getApplicationContext());
        Surveyer surveyer = dataHelper.getSurveyer(username);
        if (surveyer != null) {
            Toast.makeText(context, "User '" + username + "' already exists!", Toast.LENGTH_SHORT).show();
            return;
        }
        surveyer = new Surveyer(username, null, null, password);
        dataHelper.insertSurveyer(surveyer);
        Toast.makeText(getApplicationContext(), "Register Successful for User - " + username, Toast.LENGTH_LONG).show();
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
        Surveyer surveyer = dataHelper.getSurveyer(username);
        if (surveyer == null) {
            Toast.makeText(getApplicationContext(), "User: " + username + " not found in Database", Toast.LENGTH_SHORT).show();
        } else if (surveyer.getPassword().contentEquals(password)) {
            Log.i(TAG, "Login Success");
            Intent intent = new Intent(MainActivity.this, ListSurveyActivity.class);
            intent.putExtra("SURVEYER_ID", surveyer.getId());
            MainActivity.this.startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Password did not match", Toast.LENGTH_SHORT).show();
        }
    }


    public void fillUp(View view) {
        firstName = firstNameET.getText().toString();
        lastName = lastNameET.getText().toString();
        // TODO
    }
}
