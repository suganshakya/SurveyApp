package survey.shakya.sugan.surveyapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import survey.shakya.sugan.surveyapp.R;
import survey.shakya.sugan.surveyapp.adapter.QuestionAdapter;
import survey.shakya.sugan.surveyapp.data.DataHelper;
import survey.shakya.sugan.surveyapp.dialogs.QuestionFragment;
import survey.shakya.sugan.surveyapp.model.Question;
import survey.shakya.sugan.surveyapp.model.Response;
import survey.shakya.sugan.surveyapp.model.User;

public class ListQuestionActivity extends AppCompatActivity {
    private static String TAG = ListQuestionActivity.class.getName();
    int userId;
    int surveyId;
    BaseAdapter questionAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        surveyId = intent.getIntExtra("SURVEY_ID", -1);
        if (surveyId == -1) {
            Toast.makeText(getApplicationContext(), "Invalid Survey ID", Toast.LENGTH_SHORT).show();
            return;
        }

        userId = intent.getIntExtra("USER_ID", -1);
        if (surveyId == -1) {
            Toast.makeText(getApplicationContext(), "Invalid User ID", Toast.LENGTH_SHORT).show();
            return;
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        DataHelper dataHelper = DataHelper.getInstance(getApplicationContext());
        User user = dataHelper.getUser(userId);

        if (user.getUserType() == User.UserType.SURVEYER) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCreateQuestionDialog();
                }
            });
        } else {
            fab.setVisibility(View.GONE);
        }

        questionAdapter = new QuestionAdapter(this, getApplicationContext(), userId, surveyId);

        if (questionAdapter == null) {      // TODO: redundant code
            Toast.makeText(getApplicationContext(), "No Question Found for survey - " + surveyId, Toast.LENGTH_SHORT).show();
            return;
        }

        listView = (ListView) findViewById(R.id.list_view_question);
        listView.setAdapter(questionAdapter);

        Button button = (Button) findViewById(R.id.button_submit_response);
        if(user.getUserType() == User.UserType.SURVEYER) {
            button.setVisibility(View.GONE);
        }
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

    public void startListResponseActivity(int questionId){
        Intent intent = new Intent(ListQuestionActivity.this, ListResponseActivity.class);
        intent.putExtra("QUESTION_ID", questionId);
        intent.putExtra("SURVEYEE_ID", -1);
        ListQuestionActivity.this.startActivity(intent);
    }

    public void submitResponse(View view) {
        List<String> strings = new ArrayList<>();
        for(int i=0; i < listView.getCount() ; ++i){
            View currentView = listView.getChildAt(i);
            TextView questionIdTV = (TextView) currentView.findViewById(R.id.text_view_question_id);
            int questionId = Integer.parseInt(questionIdTV.getText().toString());

            DataHelper dataHelper = DataHelper.getInstance(getApplicationContext());
            Question question = dataHelper.getQuestion(questionId);
            dataHelper.close();

            String responseText = null;

            switch (question.getType()){
                case Question.FILL_IN_BLANK:
                    EditText editText = (EditText) currentView.findViewById(R.id.edit_text_question_response);
                    responseText = editText.getText().toString();
                    break;
                case Question.RADIO:
                    RadioGroup radioGroup = (RadioGroup) currentView.findViewById(R.id.radio_group_question_response);
                    int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                    RadioButton selectedRadioButton = (RadioButton) radioGroup.findViewById(selectedRadioButtonId);
                    responseText = selectedRadioButton.getText().toString();
                    break;
                case Question.SPINNER:
                    Spinner spinner = (Spinner) currentView.findViewById(R.id.spinner_question_response);
                    responseText = spinner.getSelectedItem().toString();
                    break;
                case Question.TRUE_FALSE:
                    RadioGroup radioGroup1 = (RadioGroup) currentView.findViewById(R.id.radio_group_true_false_response);
                    int selectedRadioButtonId1 = radioGroup1.getCheckedRadioButtonId();
                    RadioButton selectedRadioButton1 = (RadioButton) radioGroup1.findViewById(selectedRadioButtonId1);
                    responseText = selectedRadioButton1.getText().toString();
                    break;
            }

            Response response = new Response(userId, questionId, responseText);

            dataHelper = DataHelper.getInstance(getApplicationContext());
            long result = dataHelper.replaceResponse(response);
            dataHelper.close();

            if(result == -1) {
                Snackbar.make(view, "Error: inserting a response", Snackbar.LENGTH_LONG).show();
                Log.e(TAG, "Error: inserting a response in database.");
            } else {
                Snackbar.make(view, "Success: insert a response in database.", Snackbar.LENGTH_LONG).show();
                Log.i(TAG, "Success: insert a response in database.");
            }
            strings.add(responseText);
        }
        Toast.makeText(getApplicationContext(), strings.toString(), Toast.LENGTH_LONG ).show();
    }
}
