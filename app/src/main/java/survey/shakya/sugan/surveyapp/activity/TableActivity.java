package survey.shakya.sugan.surveyapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import survey.shakya.sugan.surveyapp.R;
import survey.shakya.sugan.surveyapp.data.DataHelper;
import survey.shakya.sugan.surveyapp.model.Question;
import survey.shakya.sugan.surveyapp.model.Response;
import survey.shakya.sugan.surveyapp.model.Survey;
import survey.shakya.sugan.surveyapp.model.User;

public class TableActivity extends AppCompatActivity {
    private static String TAG = TableActivity.class.getName();

    int surveyerId;

    Spinner surveySpinner, criteriaSpinner, valueSpinner;  // criteria can be question or surveyee

    String surveyName = null;
    String criteria = "Question";   // By default

    TextView valueTV;   // Question or Surveyee depending on criteria spinner
    TableLayout tableLayout;
    List<Response> responseList;

    Question currentQuestion= null;
    User currentSurveyee = null;

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

        surveySpinner = (Spinner) findViewById(R.id.survey_spinner);
        valueTV = (TextView) findViewById(R.id.column_name_textView);

        criteriaSpinner = (Spinner) findViewById(R.id.criteria_spinner);
        valueSpinner = (Spinner) findViewById(R.id.value_spinner);
        tableLayout = (TableLayout) findViewById(R.id.view_table_layout);

        populateSurveySpinner();
        surveySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
                // TODO
                if (criteria.contentEquals("Question")) {
                    currentQuestion = (Question) parent.getItemAtPosition(position);
                } else if (criteria.contentEquals("Surveyee")) {
                    currentSurveyee = (User) parent.getItemAtPosition(position);
                }
                displayTable(criteria);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void populateSurveySpinner() {
        DataHelper dataHelper = DataHelper.getInstance(getApplicationContext());
        List<Survey> surveyList = dataHelper.getSurveyList(surveyerId);
        if (surveyList == null) {
            return;
        }
        List<String> surveyNames = new ArrayList<>();
        for (Survey survey : surveyList) {
            surveyNames.add(survey.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, surveyNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        surveySpinner.setAdapter(adapter);
    }

    private void setCriteria() {
        List<String> criteriaList = new ArrayList<>(Arrays.asList("Question", "Surveyee"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, criteriaList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        criteriaSpinner.setAdapter(adapter);
        criteriaSpinner.setSelection(0);
    }

    private void populateValueSpinner() {
        if (criteria.contentEquals("Question")) {
            DataHelper dataHelper = DataHelper.getInstance(getApplicationContext());
            Survey survey = dataHelper.getSurvey(surveyerId, surveyName);
            if (survey == null) {
                return;
            }
            List<Question> questions = dataHelper.getQuestionList(survey.getId());
            if (questions == null) {
                return;
            }

            ArrayAdapter<Question> adapter = new ArrayAdapter<Question>(this, android.R.layout.simple_spinner_item, questions);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            valueSpinner.setAdapter(adapter);
        } else if (criteria.contentEquals("Surveyee")) {
            DataHelper dataHelper = DataHelper.getInstance(getApplicationContext());
            List<User> surveyeeList = dataHelper.getAllSurveyee();  // TODO Bug to be fixed
            if (surveyeeList == null) {
                return;
            }
            ArrayAdapter<User> adapter = new ArrayAdapter<User>(this, android.R.layout.simple_spinner_item, surveyeeList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            valueSpinner.setAdapter(adapter);
        }
        displayTable(criteria);
    }

    private void displayTable(String criteria) {
        if(currentSurveyee == null && currentQuestion == null){
            return;
        }

        Context context = getApplicationContext();
        tableLayout.removeAllViews();
        String[] projection1 = new String[]{"Question ID", "Question", "Response"};
        String[] projection2 = new String[]{"Surveyee ID", "Surveyee Name", "Response"};
        TableRow headRow = new TableRow(getApplicationContext());

        headRow.setBackgroundColor(ContextCompat.getColor(context, R.color.colorTableRowHead));
        headRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
        );

        int dpToPixelValue = dpToPixel(5, context);
        String[] projection = new String[]{};
        if (criteria.equals("Question")) {
            projection = projection2;
        } else if (criteria.equals("Surveyee")) {
            projection = projection1;
        }
        for (int i = 0; i < projection.length; ++i) {
            TextView textView = new TextView(context);
            textView.setPadding(dpToPixelValue, dpToPixelValue, dpToPixelValue, dpToPixelValue);
            textView.setText(projection[i].toUpperCase());
            textView.setBackgroundResource(R.drawable.row_border);
            textView.setTypeface(null, Typeface.BOLD);
            headRow.addView(textView);
        }

        tableLayout.addView(headRow, new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        DataHelper dataHelper = DataHelper.getInstance(context);
        if (criteria.contentEquals("Question")) {
            if(currentQuestion == null){
                return;
            }
            responseList = dataHelper.getResponseListByQuestion(currentQuestion.getId());
        } else {
            if(currentSurveyee == null){
                return;
            }
            responseList = dataHelper.getResponseListBySurveyee(currentSurveyee.getId());
        }

        int count = 0;

        System.out.println(responseList);

        if(responseList == null){
            return;
        }

        for (Response response : responseList) {
            TableRow tablerow = new TableRow(context);
            if (count % 2 == 0) {
                tablerow.setBackgroundColor(ContextCompat.getColor(context, R.color.colorTableRowEven));
            } else {
                tablerow.setBackgroundColor(ContextCompat.getColor(context, R.color.colorTableRowOdd));
            }
            count++;
            tablerow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            for (int i = 0; i < projection.length; ++i) {
                TextView textView = new TextView(context);
                textView.setPadding(dpToPixelValue, dpToPixelValue, dpToPixelValue, dpToPixelValue);
                textView.setText(response.getValue(getApplicationContext(), projection[i]));
                textView.setBackgroundResource(R.drawable.row_border);
                tablerow.addView(textView);
            }

            tableLayout.addView(tablerow, new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        }
    }

    private int dpToPixel(int dp, Context context) {
        Float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((float) dp * scale);
    }
}
