package survey.shakya.sugan.surveyapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

public class QuestionAdapterForSurveyer extends BaseAdapter {
    private static final int TYPE_COUNT = 1;
    private Context context;
    List<Question> questionList = new ArrayList<>();
    private int surveyId;

    public QuestionAdapterForSurveyer(Context context, int surveyId) {
        this.surveyId = surveyId;
        this.context = context;
        DataHelper helper = DataHelper.getInstance(this.context);
        questionList = helper.getQuestionList(surveyId);
        Toast.makeText(context, "QuestionAdapter -" + surveyId, Toast.LENGTH_LONG).show();
    }

    @Override
    public int getCount() {
        if(questionList == null){
            return 0;
        }
        return questionList.size();
    }

    @Override
    public Object getItem(int position) {
        if(questionList == null){
            return null;
        }
        return questionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            if(questionList == null){
                Toast.makeText(context, "No Question Found for the Survey in Database.", Toast.LENGTH_SHORT).show();
                return view;
            }
            Question question = questionList.get(position);
            LayoutInflater layoutInflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = layoutInflater.inflate(R.layout.view_question_layout_for_surveyer, parent, false);

            TextView questionIdTextView = (TextView) view.findViewById(R.id.text_view_question_id);
            questionIdTextView.setText(question.getId());

            TextView questionQuestionTextView = (TextView) view.findViewById(R.id.text_view_question_question);
            questionQuestionTextView.setText(question.getQuestion());

            TextView questionTypeTextView = (TextView) view.findViewById(R.id.text_view_question_type_for_surveyer);
            questionTypeTextView.setText(question.getType());

            TextView questionOptionsTextView = (TextView) view.findViewById(R.id.text_view_question_options_for_surveyer);
            questionOptionsTextView.setText(question.getOptions());

            TextView questionSurveyTextView = (TextView) view.findViewById(R.id.text_view_question_survey_for_surveyer);
            questionSurveyTextView.setText(question.getSurveyId());
        }
        return view;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }
}
