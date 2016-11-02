package survey.shakya.sugan.surveyapp.activity;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import survey.shakya.sugan.surveyapp.R;
import survey.shakya.sugan.surveyapp.adapter.SurveyAdapter;
import survey.shakya.sugan.surveyapp.data.DataHelper;
import survey.shakya.sugan.surveyapp.dialogs.SurveyFragment;
import survey.shakya.sugan.surveyapp.model.Survey;
import survey.shakya.sugan.surveyapp.model.User;

public class ListSurveyActivity extends AppCompatActivity {
    private static String TAG = ListSurveyActivity.class.getName();
    User user;
    SurveyAdapter surveyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_survey);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        int userId = intent.getIntExtra("USER_ID", -1);
        if (userId == -1) {
            Toast.makeText(getApplicationContext(), "Invalid User ID", Toast.LENGTH_SHORT).show();
            return;
        }

        DataHelper dataHelper = DataHelper.getInstance(getApplicationContext());
        user = dataHelper.getUser(userId);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        if (user.getUserType() == User.UserType.SURVEYEE) {
            fab.hide();     // No need for Surveyee
        } else {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCreateSurveyDialog();
                }
            });
        }

        TextView textView = (TextView) findViewById(R.id.surveyer_name);
        textView.setText(user.getUserTypeString() + " : " + user.getName());

        surveyAdapter = new SurveyAdapter(this, getApplicationContext(), user);
        if (surveyAdapter == null) {    // TODO: redundant code --- check it
            Toast.makeText(getApplicationContext(), "No Survey Found for userId - " + userId, Toast.LENGTH_SHORT).show();
            return;
        } else {
            ListView listView = (ListView) findViewById(R.id.list_view_survey);
            listView.setAdapter(surveyAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.i(TAG, "Item " + position + " Selected");
                    Survey survey = (Survey) surveyAdapter.getItem(position);
                    listQuestion(user.getId(), survey.getId());
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.survey_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.survey_settings) {
            // TODO: 01/11/16
            if(user.getUserType() == User.UserType.SURVEYER) {
                Intent intent = new Intent(ListSurveyActivity.this, TableActivity.class);
                intent.putExtra("SURVEYER_ID", user.getId());
                ListSurveyActivity.this.startActivity(intent);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "List of Survey Resumed.");
        super.onResume();
//        surveyAdapter.updateData();
    }

    public void showCreateSurveyDialog() {       // Only for Surveyer
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("SurveyDialogFragment");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment surveyFragment = SurveyFragment.newInstance(user.getId());
        surveyFragment.show(ft, "SurveyDialogFragment");
    }

    public void listQuestion(int userId, int surveyId) {
        Intent intent = new Intent(ListSurveyActivity.this, ListQuestionActivity.class);
        intent.putExtra("USER_ID", userId);
        intent.putExtra("SURVEY_ID", surveyId);
        ListSurveyActivity.this.startActivity(intent);
    }

    public void startListResponseActivity(int surveyId){
        Intent intent = new Intent(ListSurveyActivity.this, ListResponseActivity.class);
        intent.putExtra("QUESTION_ID", -1);
        intent.putExtra("SURVEY_ID", surveyId);
        ListSurveyActivity.this.startActivity(intent);
    }
}
