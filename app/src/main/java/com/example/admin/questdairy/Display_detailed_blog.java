package com.example.admin.questdairy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Admin on 8/10/2017.
 */

public class Display_detailed_blog extends Fragment {

    @BindView(R.id.title)
    TextView title_tv;
    @BindView(R.id.context)
    TextView context_tv;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.time)
    TextView time;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_detailed_blog, null);
        unbinder = ButterKnife.bind(this, view);

        SharedPreferences sp = this.getActivity().getSharedPreferences("QuestDiary",getContext().MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("farg","3");
        e.commit();

        Bundle bundle = getArguments();
        String title = bundle.getString("title");
        String detail = bundle.getString("detail");
        String date1 = bundle.getString("date");
        String time1 = bundle.getString("time");

        title_tv.setText(title);
        context_tv.setText(detail);
        date.setText(date1);
        time.setText(time1);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
