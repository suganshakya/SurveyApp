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
import survey.shakya.sugan.surveyapp.adapter.QuestionAdapterForSurveyee;
import survey.shakya.sugan.surveyapp.adapter.QuestionAdapterForSurveyer;
import survey.shakya.sugan.surveyapp.dialogs.QuestionFragment;
import survey.shakya.sugan.surveyapp.dialogs.SurveyFragment;
import survey.shakya.sugan.surveyapp.dialogs.UpdateQuestionFragment;
import survey.shakya.sugan.surveyapp.model.Question;
import survey.shakya.sugan.surveyapp.model.Survey;

public class ListQuestionForSurveyerActivity extends AppCompatActivity {
    private static String TAG = ListQuestionForSurveyerActivity.class.getName();

    int surveyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_question_for_surveyer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        surveyId = intent.getIntExtra("SURVEY_ID", -1);
        if (surveyId == -1) {
            Toast.makeText(getApplicationContext(), "Invalid Survey ID", Toast.LENGTH_SHORT).show();
            return;
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateQuestionDialog();
            }
        });

        final QuestionAdapterForSurveyer questionAdapter = new QuestionAdapterForSurveyer(getApplicationContext(), surveyId);
        if (questionAdapter == null) {
            Toast.makeText(getApplicationContext(), "No Question Found for survey - " + surveyId, Toast.LENGTH_SHORT).show();
            return;
        }

        ListView listView = (ListView) findViewById(R.id.list_view_question_for_surveyer);
        listView.setAdapter(questionAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "Item " + position +" Selected");
                Question question = (Question) questionAdapter.getItem(position);
                showUpdateQuestionDialog(question.getId());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void showCreateQuestionDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("QuestionDialogFragment");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment questionFragment = QuestionFragment.newInstance(surveyId);
        questionFragment.show(ft, "QuestionDialogFragment");
    }

    public void showUpdateQuestionDialog(int questionId){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("UpdateQuestionDialogFragment");
        if(prev != null){
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment surveyFragment = UpdateQuestionFragment.newInstance(questionId);
        surveyFragment.show(ft, "UpdateQuestionDialogFragment");
    }
}
