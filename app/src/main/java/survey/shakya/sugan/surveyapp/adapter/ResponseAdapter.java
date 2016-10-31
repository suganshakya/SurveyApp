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
import survey.shakya.sugan.surveyapp.model.Response;
import survey.shakya.sugan.surveyapp.model.Survey;
import survey.shakya.sugan.surveyapp.model.User;

/**
 * Created by sugan on 08/10/16.
 */

public class ResponseAdapter extends BaseAdapter {
    private static final int TYPE_COUNT = 1;
    private Context context;
    List<Response> responseList = new ArrayList<>();
    User user;
    boolean isByUser;

    public ResponseAdapter(Context context, boolean isByUser,  User user, int questionId) {
        this.context = context;
        this.user = user;
        DataHelper helper = DataHelper.getInstance(this.context);
        if(isByUser) {
            responseList = helper.getResponseListBySurveyee(user.getId());
        } else {
            responseList = helper.getResponseListByQuestion(questionId);
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
            if(isByUser) {
                referenceTV.setText(""+ response.getSurveyeeId());
            } else {
                referenceTV.setText("" + response.getQuestionId());
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
