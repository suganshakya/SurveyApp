package survey.shakya.sugan.surveyapp.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import survey.shakya.sugan.surveyapp.R;
import survey.shakya.sugan.surveyapp.adapter.SurveyAdapter;
import survey.shakya.sugan.surveyapp.adapter.SurveyAdapterForSurveyee;
import survey.shakya.sugan.surveyapp.dialogs.SurveyFragment;
import survey.shakya.sugan.surveyapp.model.Survey;

public class ListSurveyActivityForSurveyee extends AppCompatActivity {
    private static String TAG = ListSurveyActivityForSurveyee.class.getName();
    int surveyeeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_survey);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        surveyeeId = intent.getIntExtra("SURVEYEE_ID", -1);
        if(surveyeeId == -1){
            Toast.makeText(getApplicationContext(), "Invalid Surveyer ID", Toast.LENGTH_SHORT).show();
            return;
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showCreateSurveyDialog();
//            }
//        });



        final SurveyAdapterForSurveyee surveyAdapter = new SurveyAdapterForSurveyee(getApplicationContext(), surveyeeId);
        if (surveyAdapter == null) {
            Toast.makeText(getApplicationContext(), "No Survey Found", Toast.LENGTH_SHORT).show();
            return;
        } else {
            ListView listView = (ListView) findViewById(R.id.list_view_survey);
            listView.setAdapter(surveyAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.i(TAG, "Item " + position +" Selected");
                    Survey survey = (Survey) surveyAdapter.getItem(position);
                    listQuestion(surveyeeId, survey.getId());
                }
            });
        }
    }

    public void listQuestion(int surveyeeId, int surveyId) {
        Intent intent = new Intent(ListSurveyActivityForSurveyee.this, ListQuestionForSurveyeeActivity.class);
        intent.putExtra("SURVEYEE_ID", surveyeeId);
        intent.putExtra("SURVEY_ID", surveyId);
        ListSurveyActivityForSurveyee.this.startActivity(intent);
    }
}
