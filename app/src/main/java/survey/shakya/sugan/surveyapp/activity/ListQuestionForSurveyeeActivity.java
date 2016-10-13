package survey.shakya.sugan.surveyapp.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import survey.shakya.sugan.surveyapp.R;
import survey.shakya.sugan.surveyapp.adapter.QuestionAdapterForSurveyee;

public class ListQuestionForSurveyeeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_question_for_surveyee);
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

        int surveeId = savedInstanceState.getInt("surveeId");
        QuestionAdapterForSurveyee questionAdapter = new QuestionAdapterForSurveyee(getApplicationContext(), surveeId);
        ListView listView = (ListView) findViewById(R.id.list_view_question_for_surveyee);
        listView.setAdapter(questionAdapter);
    }

}
