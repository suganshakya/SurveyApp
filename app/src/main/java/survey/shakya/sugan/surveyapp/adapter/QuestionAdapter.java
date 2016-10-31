package survey.shakya.sugan.surveyapp.adapter;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import survey.shakya.sugan.surveyapp.R;
import survey.shakya.sugan.surveyapp.activity.ListQuestionActivity;
import survey.shakya.sugan.surveyapp.data.DataHelper;
import survey.shakya.sugan.surveyapp.dialogs.UpdateQuestionFragment;
import survey.shakya.sugan.surveyapp.model.Question;
import survey.shakya.sugan.surveyapp.model.User;

/**
 * Created by sugan on 08/10/16.
 */

public class QuestionAdapter extends BaseAdapter {
    private static String TAG = QuestionAdapter.class.getName();

    private static final int TYPE_COUNT = 4;

    private AppCompatActivity activity;
    private Context context;
    List<Question> questionList = new ArrayList<>();
    private int surveyId;
    private int userId;
    private User user;

    public QuestionAdapter(AppCompatActivity activity, Context context, int userId, int surveyId) {
        this.activity =activity;
        this.context = context;
        this.surveyId = surveyId;
        this.userId = userId;
        DataHelper helper = DataHelper.getInstance(this.context);
        user = helper.getUser(userId);
        questionList = helper.getQuestionList(surveyId);
    }

    @Override
    public int getCount() {
        if (questionList == null)
            return 0;
        return questionList.size();
    }

    @Override
    public Object getItem(int position) {
        if (questionList == null) {
            return null;
        }
        return questionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            if (questionList == null) {
                Toast.makeText(context, "No Question Found for Survey", Toast.LENGTH_SHORT).show();
                return view;
            }
            final Question question = questionList.get(position);
            LayoutInflater layoutInflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            TextView questionIdTextView;
            TextView questionQuestionTextView;
            switch (question.getType()) {
                case Question.FILL_IN_BLANK:
                    view = layoutInflater.inflate(R.layout.view_question_text_layout, parent, false);
                    questionIdTextView = (TextView) view.findViewById(R.id.text_view_question_id);
                    questionIdTextView.setText("" + question.getId());
                    questionQuestionTextView = (TextView) view.findViewById(R.id.text_view_question_question);
                    questionQuestionTextView.setText(question.getQuestion());

                    EditText questionResponseEditText = (EditText) view.findViewById(R.id.edit_text_question_response);
                    break;

                case Question.TRUE_FALSE:
                    view = layoutInflater.inflate(R.layout.view_question_true_false_layout, parent, false);

                    questionIdTextView = (TextView) view.findViewById(R.id.text_view_question_id);
                    questionIdTextView.setText("" + question.getId());
                    questionQuestionTextView = (TextView) view.findViewById(R.id.text_view_question_question);
                    questionQuestionTextView.setText(question.getQuestion());

                    RadioGroup trueFalseGroup = (RadioGroup) view.findViewById(R.id.radio_group_true_false_response);
                    String[] options = new String[]{"True", "False"};
                    RadioButton rb[] = new RadioButton[options.length];
                    rb[0] = (RadioButton) view.findViewById(R.id.true_button);
                    rb[1] = (RadioButton) view.findViewById(R.id.false_button);

                    for (int i = 0; i < options.length; ++i) {
                        rb[i].setText(options[i]);
                    }
                    break;

                case Question.SPINNER:
                    view = layoutInflater.inflate(R.layout.view_question_spinner_layout, parent, false);
                    questionIdTextView = (TextView) view.findViewById(R.id.text_view_question_id);
                    questionIdTextView.setText("" + question.getId());
                    questionQuestionTextView = (TextView) view.findViewById(R.id.text_view_question_question);
                    questionQuestionTextView.setText(question.getQuestion());

                    Spinner spinner = (Spinner) view.findViewById(R.id.spinner_question_response);
                    String[] spinnerOptions = question.getOptions().split(",");

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(parent.getContext(),
                            android.R.layout.simple_spinner_item, spinnerOptions);
                    // set the view for the Drop down list
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // set the ArrayAdapter to the spinner
                    spinner.setAdapter(dataAdapter);
                    spinner.setSelection(0);
                    break;

                case Question.RADIO:
                    view = layoutInflater.inflate(R.layout.view_question_radio_layout, parent, false);
                    questionIdTextView = (TextView) view.findViewById(R.id.text_view_question_id);
                    questionIdTextView.setText("" + question.getId());
                    questionQuestionTextView = (TextView) view.findViewById(R.id.text_view_question_question);
                    questionQuestionTextView.setText(question.getQuestion());

                    RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_group_question_response);
                    String[] radioOptions = question.getOptions().split(",");
                    for (int i = 0; i < radioOptions.length; ++i) {
                        RadioButton radioButton = new RadioButton(parent.getContext());
                        radioButton.setText(radioOptions[i]);
                        radioButton.setId(i);
                        radioGroup.addView(radioButton, new RadioGroup.LayoutParams(
                                RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT)
                        );
                    }
                    break;
            }

            ImageButton editButton = (ImageButton) view.findViewById(R.id.edit_icon);
            if(user.getUserType() == User.UserType.SURVEYER) {

                editButton.setVisibility(View.VISIBLE);
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                        Fragment prev = activity.getSupportFragmentManager().findFragmentByTag("UpdateQuestionDialogFragment");
                        if (prev != null) {
                            ft.remove(prev);
                        }
                        ft.addToBackStack(null);

                        DialogFragment surveyFragment = UpdateQuestionFragment.newInstance(question.getId());
                        surveyFragment.show(ft, "UpdateQuestionDialogFragment");
                    }
                });

            } else {
                editButton.setVisibility(View.GONE);
            }

            ImageButton viewButton = (ImageButton) view.findViewById(R.id.view_icon);
            if(user.getUserType() == User.UserType.SURVEYER) {
                viewButton.setVisibility(View.VISIBLE);
                viewButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(activity instanceof ListQuestionActivity){
                            ((ListQuestionActivity) activity).startListResponseActivity(question.getId());
                        }
                    }
                });
            } else {
                viewButton.setVisibility(View.GONE);
            }

            ImageButton deleteButton = (ImageButton) view.findViewById(R.id.delete_icon);
            if(user.getUserType() == User.UserType.SURVEYER) {
                deleteButton.setVisibility(View.VISIBLE);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DataHelper dataHelper = DataHelper.getInstance(context);
                        dataHelper.deleteQuestion(question.getId());
                        Toast.makeText(context, "Question " + question.getId() + " deleted.", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                deleteButton.setVisibility(View.GONE);
            }
        }
        return view;
    }

}
