package survey.shakya.sugan.surveyapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import survey.shakya.sugan.surveyapp.R;
import survey.shakya.sugan.surveyapp.data.DataHelper;
import survey.shakya.sugan.surveyapp.model.Question;
import survey.shakya.sugan.surveyapp.model.Survey;

/**
 * Created by sugan on 08/10/16.
 */

public class QuestionAdapterForSurveyee extends BaseAdapter {
    private static final int TYPE_COUNT = 4;

    private Context context;
    List<Question> questionList = new ArrayList<>();
    private int surveyId;
    private int surveyeeId;

    public QuestionAdapterForSurveyee(Context context, int surveyeeId, int surveyId) {
        this.context = context;
        this.surveyId = surveyId;
        this.surveyeeId = surveyeeId;
        DataHelper helper = DataHelper.getInstance(this.context);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            if (questionList == null) {
                Toast.makeText(context, "No Question Found for Survey", Toast.LENGTH_SHORT).show();
                return view;
            }
            Question question = questionList.get(position);
            LayoutInflater layoutInflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            TextView questionIdTextView;
            TextView questionQuestionTextView;
            switch (question.getType()) {
                case Question.FILL_IN_BLANK:
                    view = layoutInflater.inflate(R.layout.view_question_text_layout_for_surveyee, parent, false);
                    questionIdTextView = (TextView) view.findViewById(R.id.text_view_question_id);
                    questionIdTextView.setText("" + question.getId());
                    questionQuestionTextView = (TextView) view.findViewById(R.id.text_view_question_question);
                    questionQuestionTextView.setText(question.getQuestion());

                    EditText questionResponseEditText = (EditText) view.findViewById(R.id.edit_text_question_response_for_surveyer);
                    break;

                case Question.TRUE_FALSE:
                    view = layoutInflater.inflate(R.layout.view_question_true_false_layout_for_surveyee, parent, false);

                    questionIdTextView = (TextView) view.findViewById(R.id.text_view_question_id);
                    questionIdTextView.setText("" + question.getId());
                    questionQuestionTextView = (TextView) view.findViewById(R.id.text_view_question_question);
                    questionQuestionTextView.setText(question.getQuestion());

                    RadioGroup trueFalseGroup = (RadioGroup) view.findViewById(R.id.true_false_group_question_for_surveyee);
                    String[] options = new String[]{"True", "False"};
                    RadioButton rb[] = new RadioButton[options.length];
                    rb[0] = (RadioButton) view.findViewById(R.id.true_button);
                    rb[1] = (RadioButton) view.findViewById(R.id.false_button);

                    for (int i = 0; i < options.length; ++i) {
                        rb[i].setText(options[i]);
                    }
                    break;

                case Question.SPINNER:
                    view = layoutInflater.inflate(R.layout.view_question_spinner_layout_for_surveyee, parent, false);
                    questionIdTextView = (TextView) view.findViewById(R.id.text_view_question_id);
                    questionIdTextView.setText("" + question.getId());
                    questionQuestionTextView = (TextView) view.findViewById(R.id.text_view_question_question);
                    questionQuestionTextView.setText(question.getQuestion());

                    Spinner spinner = (Spinner) view.findViewById(R.id.spinner_view_question_for_surveyee);
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
                    view = layoutInflater.inflate(R.layout.view_question_radio_layout_for_surveyee, parent, false);
                    questionIdTextView = (TextView) view.findViewById(R.id.text_view_question_id);
                    questionIdTextView.setText(""+ question.getId());
                    questionQuestionTextView = (TextView) view.findViewById(R.id.text_view_question_question);
                    questionQuestionTextView.setText(question.getQuestion());

                    RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_group_view_question_surveyee);
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
        }
        return view;
    }
}
