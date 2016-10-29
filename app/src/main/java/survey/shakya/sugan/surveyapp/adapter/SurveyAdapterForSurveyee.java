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
import survey.shakya.sugan.surveyapp.model.Survey;

/**
 * Created by sugan on 08/10/16.
 */

public class SurveyAdapterForSurveyee extends BaseAdapter {
    private static final int TYPE_COUNT = 4;
    private Context context;
    List<Survey> surveyList = new ArrayList<>();
    int surveyeeId;

    public SurveyAdapterForSurveyee(Context context, int surveyeeId) {
        this.context = context;
        this.surveyeeId = surveyeeId;
        DataHelper helper = DataHelper.getInstance(this.context);
        surveyList = helper.getAllSurveys();
    }

    @Override
    public int getCount() {
        if(surveyList == null){
            return 0;
        }
        return surveyList.size();
    }

    @Override
    public Object getItem(int position) {
        if(surveyList == null){
            return null;
        }
        return surveyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            if(surveyList == null){
                Toast.makeText(context, "No Survey Found in Database.", Toast.LENGTH_SHORT).show();
                return view;
            }
            Survey survey = surveyList.get(position);
            LayoutInflater layoutInflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.view_survey_layout, parent, false);

            TextView surveyerIdTV = (TextView) view.findViewById(R.id.text_view_survey_id);
            surveyerIdTV.setText(survey.getId() + "");
            TextView surveyNameTV = (TextView) view.findViewById(R.id.text_view_survey_name);
            surveyNameTV.setText(survey.getName());
        }
        return view;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }
}
