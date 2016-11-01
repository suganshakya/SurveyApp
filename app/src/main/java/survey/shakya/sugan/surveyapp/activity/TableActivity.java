package survey.shakya.sugan.surveyapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import survey.shakya.sugan.surveyapp.R;
import survey.shakya.sugan.surveyapp.data.DataHelper;
import survey.shakya.sugan.surveyapp.model.Question;
import survey.shakya.sugan.surveyapp.model.Survey;
import survey.shakya.sugan.surveyapp.model.User;

public class TableActivity extends AppCompatActivity {
    private static String TAG = TableActivity.class.getName();

    DataHelper dataHelper;
    int surveyId, surveyerId;
    int id; // may be question id or surveyer-id based on criteria spinner

    Spinner surveySpinner, criteriaSpinner, valueSpinner;  // criteria can be question or surveyee
    String surveyName = null, criteria = null, value = null;
    TextView valueTV;   // Question or Surveyee depending on criteria spinner
    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
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

        setTitle("Table");
        Intent intent = getIntent();
        surveyerId = intent.getIntExtra("SURVEYER_ID", -1);

        dataHelper = DataHelper.getInstance(getApplicationContext());

        surveySpinner = (Spinner) findViewById (R.id.survey_spinner);
        valueTV = (TextView) findViewById(R.id.column_name_textView);

        criteriaSpinner = (Spinner) findViewById(R.id.criteria_spinner);
        valueSpinner = (Spinner) findViewById(R.id.value_spinner);
        tableLayout = (TableLayout) findViewById(R.id.view_table_layout);

        populateSurveySpinner();
        surveySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, print());

                surveyName = (String) parent.getItemAtPosition(position);
                populateValueSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        setCriteria();
        criteriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, print());

                criteria = (String) parent.getItemAtPosition(position);
                valueTV.setText(criteria);
                populateValueSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        valueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, print());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void populateSurveySpinner(){
        List<Survey> surveyList = dataHelper.getSurveyList(surveyerId);
        if(surveyList == null){
            return;
        }
        List<String> surveyNames = new ArrayList<>();
        for(Survey survey:surveyList){
            surveyNames.add(survey.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, surveyNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        surveySpinner.setAdapter(adapter);
    }

    private void setCriteria(){
        List<String> criteriaList = new ArrayList<>(Arrays.asList("Question", "Surveyee"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, criteriaList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        criteriaSpinner.setAdapter(adapter);
    }

    private void populateValueSpinner(){
        if(criteria == null){
            return;
        }
        List<String> values = new ArrayList<String>();
        if(criteria.contentEquals("Question")){
            Survey survey = dataHelper.getSurvey(surveyerId, surveyName);
            List<Question> questions = dataHelper.getQuestionList(survey.getId());
            if(questions == null){
                return;
            }
            for(Question question:questions){
                values.add(question.getQuestion());
            }
            populateValueSpinner(values);
        } else if (criteria.contentEquals("Surveyee")){
//                    List<User> user = dataHelper.getAllSurveyee();
            // TODO
        }
    }

    private void populateValueSpinner(List<String> values){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        valueSpinner.setAdapter(adapter);
    }

    public String print(){
        return "Survey = " + surveyName + ", Criteria: " + criteria + ", Value: " + value;
    }


}
