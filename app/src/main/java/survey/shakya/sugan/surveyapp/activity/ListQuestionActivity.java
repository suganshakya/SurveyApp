package survey.shakya.sugan.surveyapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import survey.shakya.sugan.surveyapp.R;
import survey.shakya.sugan.surveyapp.adapter.QuestionAdapter;
import survey.shakya.sugan.surveyapp.data.DataHelper;
import survey.shakya.sugan.surveyapp.dialogs.QuestionFragment;
import survey.shakya.sugan.surveyapp.model.Question;
import survey.shakya.sugan.surveyapp.model.Response;
import survey.shakya.sugan.surveyapp.model.User;

import static survey.shakya.sugan.surveyapp.model.Question.Type.FILL_IN_THE_BLANK;
import static survey.shakya.sugan.surveyapp.model.Question.Type.RADIO;
import static survey.shakya.sugan.surveyapp.model.Question.Type.SPINNER;
import static survey.shakya.sugan.surveyapp.model.Question.Type.TRUE_FALSE;

public class ListQuestionActivity extends AppCompatActivity {
    private static String TAG = ListQuestionActivity.class.getName();
    int userId;
    int surveyId;
    BaseAdapter questionAdapter;
    ListView listView;
    User user;

    private static final int READ_REQUEST_CODE = 42;


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
        user = dataHelper.getUser(userId);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        if(user.getUserType() == User.UserType.SURVEYER) {
            getMenuInflater().inflate(R.menu.question_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.question_settings) {
            if(user.getUserType() == User.UserType.SURVEYER) {
                performFileSearch();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void performFileSearch(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");      //  Android BUG ("*.csv|*.txt")
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if(requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Uri uri = null;
            if(resultData!=null){
                uri = resultData.getData();
                Toast.makeText(getApplicationContext(), "Uri = " + uri , Toast.LENGTH_LONG).show();
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                DataHelper dataHelper = DataHelper.getInstance(getApplicationContext());
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            inputStream));
                    while ((line = reader.readLine()) != null) {
                        Question question = new Question();
                        String [] words = line.split(",");
                        question.setSurveyId(user.getId());
                        question.setSurveyId(surveyId);
                        question.setQuestion(words[0]);
                        String questionTypeHint = words[1].toLowerCase();
                        if(questionTypeHint.contains("fill")){
                            question.setType(FILL_IN_THE_BLANK);
                            dataHelper.insertQuestion(question);
                            continue;
                        } else if (questionTypeHint.contains("true")){
                            question.setType(TRUE_FALSE);
                            dataHelper.insertQuestion(question);
                            continue;
                        } else if (questionTypeHint.contains("radio")){
                            question.setType(RADIO);
                        } else if (questionTypeHint.contains("spin")) {
                            question.setType(SPINNER);
                        } else {
                            Toast.makeText(getApplicationContext(), "Error settting question Type", Toast.LENGTH_LONG).show();
                            return;
                        }
                        int index = line.indexOf("," , 1 + line.indexOf(","));
                        question.setOptions(line.substring(index + 1));
                        dataHelper.insertQuestion(question);
                    }

                    dataHelper.close();
                    inputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                Toast.makeText(getApplicationContext(), stringBuilder.toString(), Toast.LENGTH_LONG).show();
            }
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
                case FILL_IN_THE_BLANK:
                    EditText editText = (EditText) currentView.findViewById(R.id.edit_text_question_response);
                    responseText = editText.getText().toString();
                    if(! checkResponse(responseText, view)){
                        return;
                    }
                    break;
                case TRUE_FALSE:
                    RadioGroup radioGroup = (RadioGroup) currentView.findViewById(R.id.radio_group_question_response);
                    int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                    RadioButton selectedRadioButton = (RadioButton) radioGroup.findViewById(selectedRadioButtonId);
                    if(selectedRadioButton == null){
                        Snackbar.make(view, "Response should not be empty.", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    responseText = selectedRadioButton.getText().toString();
                    break;
                case SPINNER:
                    Spinner spinner = (Spinner) currentView.findViewById(R.id.spinner_question_response);
                    responseText = spinner.getSelectedItem().toString();
                    if(! checkResponse(responseText, view)){
                        return;
                    }
                    break;
                case RADIO:
                    RadioGroup radioGroup1 = (RadioGroup) currentView.findViewById(R.id.radio_group_true_false_response);
                    int selectedRadioButtonId1 = radioGroup1.getCheckedRadioButtonId();
                    RadioButton selectedRadioButton1 = (RadioButton) radioGroup1.findViewById(selectedRadioButtonId1);
                    responseText = selectedRadioButton1.getText().toString();
                    if(! checkResponse(responseText, view)){
                        return;
                    }
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

    private boolean checkResponse(String response, View view){
        if(response == null || response.length() == 0){
            Snackbar.make(view, "Response should not be empty.", Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
