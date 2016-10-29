package survey.shakya.sugan.surveyapp.activity;


import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import survey.shakya.sugan.surveyapp.MainActivity;
import survey.shakya.sugan.surveyapp.R;
import survey.shakya.sugan.surveyapp.adapter.SurveyAdapter;
import survey.shakya.sugan.surveyapp.data.DataHelper;
import survey.shakya.sugan.surveyapp.dialogs.SurveyFragment;
import survey.shakya.sugan.surveyapp.model.Question;
import survey.shakya.sugan.surveyapp.model.Survey;
import survey.shakya.sugan.surveyapp.model.User;

public class ListSurveyActivity extends AppCompatActivity {
    private static String TAG = ListSurveyActivity.class.getName();
    User user;
//    int userId;
//    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_survey);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        int userId = intent.getIntExtra("USER_ID", -1);
        if(userId == -1){
            Toast.makeText(getApplicationContext(), "Invalid User ID", Toast.LENGTH_SHORT).show();
            return;
        }

        DataHelper dataHelper = DataHelper.getInstance(getApplicationContext());
        user = dataHelper.getUser(userId);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        if(user.getUserType() == User.UserType.SURVEYEE){
            fab.hide(); // No need for Surveyee
        } else {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCreateSurveyDialog();
                }
            });
        }

        final SurveyAdapter surveyAdapter = new SurveyAdapter(getApplicationContext(), user);
        if (surveyAdapter == null) {    // TODO: redundant code --- check it
            Toast.makeText(getApplicationContext(), "No Survey Found for userId - " + userId, Toast.LENGTH_SHORT).show();
            return;
        } else {
            ListView listView = (ListView) findViewById(R.id.list_view_survey);
            listView.setAdapter(surveyAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.i(TAG, "Item " + position +" Selected");
                    Survey survey = (Survey) surveyAdapter.getItem(position);
                    listQuestion(survey.getId());
                }
            });
        }
    }

    public void showCreateSurveyDialog(){       // Only for Surveyer
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("SurveyDialogFragment");
        if(prev != null){
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment surveyFragment = SurveyFragment.newInstance(user.getId());
        surveyFragment.show(ft, "SurveyDialogFragment");
    }

    public void listQuestion(int surveyId) {
        Intent intent;
        if(user.getUserType() == User.UserType.SURVEYER) {
            intent = new Intent(ListSurveyActivity.this, ListQuestionForSurveyerActivity.class);
        } else {
            intent = new Intent(ListSurveyActivity.this, ListQuestionForSurveyeeActivity.class);
        }
        intent.putExtra("SURVEY_ID", surveyId);
        ListSurveyActivity.this.startActivity(intent);
    }
}
