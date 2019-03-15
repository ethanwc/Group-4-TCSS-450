package ethanwc.tcss450.uw.edu.template.Weather;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ethanwc.tcss450.uw.edu.template.Main.WaitFragment;
import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.model.HourlyWeather;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class HourlyWeatherFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    public static final String ARG_HOURLYWEATHER_LIST = "hourly weather list";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private List<HourlyWeather> mHourlyWeather;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HourlyWeatherFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mHourlyWeather =  new ArrayList<HourlyWeather>(
                    Arrays.asList((HourlyWeather[]) getArguments().getSerializable(ARG_HOURLYWEATHER_LIST)));
            //mColumnCount = mHourlyWeather.size();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hourlyweather_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount, GridLayoutManager.HORIZONTAL, false));
            }
            recyclerView.setAdapter(new MyHourlyWeatherRecyclerViewAdapter(mHourlyWeather));
        }
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
