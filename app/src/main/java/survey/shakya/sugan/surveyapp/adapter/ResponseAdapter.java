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
import survey.shakya.sugan.surveyapp.model.Response;
import survey.shakya.sugan.surveyapp.model.User;

/**
 * Created by sugan on 08/10/16.
 */

public class ResponseAdapter extends BaseAdapter {
    private static final int TYPE_COUNT = 1;
    private Context context;
    List<Response> responseList = new ArrayList<>();
    boolean isBySurveyee;
    int id;     // either question-id or surveyee-id as per isBySurvey

    public ResponseAdapter(Context context, boolean isBySurveyee,  int id) {
        this.context = context;
        this.isBySurveyee = isBySurveyee;
        this.id = id;
        DataHelper helper = DataHelper.getInstance(this.context);
        if(isBySurveyee) {
            responseList = helper.getResponseListBySurveyee(id);
        } else {
            responseList = helper.getResponseListByQuestion(id);
        }
    }

    @Override
    public int getCount() {
        if(responseList == null){
            return 0;
        }
        return responseList.size();
    }

    @Override
    public Object getItem(int position) {
        if(responseList == null){
            return null;
        }
        return responseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            if(responseList == null){
                Toast.makeText(context, "No Survey Found in Database.", Toast.LENGTH_SHORT).show();
                return view;
            }


            Response response = responseList.get(position);
            LayoutInflater layoutInflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.view_response_layout, parent, false);

            TextView responseIdTV = (TextView) view.findViewById(R.id.text_view_response_id);
            responseIdTV.setText(response.getId() + "");
            TextView referenceTV = (TextView) view.findViewById(R.id.text_view_question_or_surveyee_name);
            if(isBySurveyee) {
                DataHelper dataHelper = DataHelper.getInstance(context);
                Question question = dataHelper.getQuestion(id);
                referenceTV.setText("Question: " + question.getQuestion());
            } else {
                DataHelper dataHelper = DataHelper.getInstance(context);
                User user = dataHelper.getUser(response.getSurveyeeId());
                referenceTV.setText("Surveyee: " + user.getName());
            }
            TextView responseTextTV = (TextView) view.findViewById(R.id.text_view_response_text1);
            responseTextTV.setText(response.getResponse());
        }
        return view;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }
}
