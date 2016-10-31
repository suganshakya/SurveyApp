package survey.shakya.sugan.surveyapp.adapter;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import survey.shakya.sugan.surveyapp.R;
import survey.shakya.sugan.surveyapp.activity.ListQuestionActivity;
import survey.shakya.sugan.surveyapp.activity.ListSurveyActivity;
import survey.shakya.sugan.surveyapp.data.DataHelper;
import survey.shakya.sugan.surveyapp.dialogs.UpdateQuestionFragment;
import survey.shakya.sugan.surveyapp.dialogs.UpdateSurveyFragment;
import survey.shakya.sugan.surveyapp.model.Survey;
import survey.shakya.sugan.surveyapp.model.User;

/**
 * Created by sugan on 08/10/16.
 */

public class SurveyAdapter extends BaseAdapter {
    private static String TAG = SurveyAdapter.class.getName();
    private static final int TYPE_COUNT = 1;
    AppCompatActivity activity;
    private Context context;
    List<Survey> surveyList = new ArrayList<>();
    User user;

    public SurveyAdapter(AppCompatActivity activity, Context context, User user) {
        this.activity = activity;
        this.context = context;
        this.user = user;
        DataHelper helper = DataHelper.getInstance(this.context);
        if(user.getUserType() == User.UserType.SURVEYER) {
            surveyList = helper.getSurveyList(user.getId());
        } else {
            surveyList = helper.getAllSurveys();
        }
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
            if (surveyList == null) {
                Toast.makeText(context, "No Survey Found in Database.", Toast.LENGTH_SHORT).show();
                return view;
            }
            final Survey survey = surveyList.get(position);
            LayoutInflater layoutInflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.view_survey_layout, parent, false);

            TextView surveyIdTV = (TextView) view.findViewById(R.id.text_view_survey_id);
            surveyIdTV.setText("" + survey.getId());

            TextView surveyNameTV = (TextView) view.findViewById(R.id.text_view_survey_name);
            Spannable content = new SpannableString(survey.getName());
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            surveyNameTV.setText(content);

            surveyNameTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Edit Question
                    if(activity instanceof ListSurveyActivity){
                        ((ListSurveyActivity) activity).listQuestion(user.getId(), survey.getId());
                    }
                }
            });


        ImageButton editButton = (ImageButton) view.findViewById(R.id.edit_icon);
        if (user.getUserType() == User.UserType.SURVEYER) {
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

                    DialogFragment surveyFragment = UpdateSurveyFragment.newInstance(survey.getId());
                    surveyFragment.show(ft, "UpdateQuestionDialogFragment");
                }
            });

        } else {
            editButton.setVisibility(View.GONE);
        }

        ImageButton viewButton = (ImageButton) view.findViewById(R.id.view_icon);
        if (user.getUserType() == User.UserType.SURVEYER) {
            viewButton.setVisibility(View.VISIBLE);
            viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                // TODO
                }
            });
        } else {
            viewButton.setVisibility(View.GONE);
        }

        ImageButton deleteButton = (ImageButton) view.findViewById(R.id.delete_icon);
        if (user.getUserType() == User.UserType.SURVEYER) {
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataHelper dataHelper = DataHelper.getInstance(context);
                    dataHelper.deleteSurvey(survey.getId());
                    Toast.makeText(context, "Survey " + survey.getId() + " deleted.", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            deleteButton.setVisibility(View.GONE);
        }
    }
        return view;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    public void updateData(){
        DataHelper helper = DataHelper.getInstance(this.context);
        if(user.getUserType() == User.UserType.SURVEYER) {
            surveyList = helper.getSurveyList(user.getId());
        } else {
            surveyList = helper.getAllSurveys();
        }
        this.notifyDataSetChanged();
    }
}
