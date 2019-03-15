package ethanwc.tcss450.uw.edu.template.Weather;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ethanwc.tcss450.uw.edu.template.Main.WaitFragment;
import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.model.DailyWeather;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class DailyWeatherFragment extends Fragment {

    public static final String ARG_DAILYWEATHER_LIST = "daily weather list";
    private int mColumnCount = 1;
    private List<DailyWeather> mDailyWeather;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DailyWeatherFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mDailyWeather = new ArrayList<DailyWeather>(
                    Arrays.asList((DailyWeather[]) getArguments().getSerializable(ARG_DAILYWEATHER_LIST)));
            //mColumnCount = mDailyWeather.size();
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Log.e("create view", " hi" + mColumnCount);
        View view = inflater.inflate(R.layout.fragment_dailyweather_list, container, false);

        int date = Calendar.DAY_OF_WEEK;
        Log.e("date:", " " + date);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount, GridLayoutManager.HORIZONTAL, false));
            }

            recyclerView.setAdapter(new MyDailyWeatherRecyclerViewAdapter(mDailyWeather));
        }

        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

}
