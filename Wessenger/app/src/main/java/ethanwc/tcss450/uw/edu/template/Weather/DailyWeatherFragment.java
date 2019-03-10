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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ethanwc.tcss450.uw.edu.template.Connections.GetAsyncTask;
import ethanwc.tcss450.uw.edu.template.Main.WaitFragment;
import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.model.DailyWeather;
import me.pushy.sdk.lib.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DailyWeatherFragment extends Fragment {

    public static final String ARG_DAILYWEATHER_LIST = "daily weather list";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private List<DailyWeather> mDailyWeather;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DailyWeatherFragment() {
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Log.e("create view", " hi" + mColumnCount);
        View view = inflater.inflate(R.layout.fragment_dailyweather_list, container, false);
        Log.e(" create view", " here");

        int date = Calendar.DAY_OF_WEEK;
        Log.e("date:", " " + date);
        new GetAsyncTask.Builder("https://api.openweathermap.org/data/2.5/forecast?zip=98403&cnt=10&appid=b0ce6ca6ee362ce9ea5bbe361fdcbf92")//uri.toString()
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleWeatherPostExecute)
                .onCancelled(this::handleErrorsInTask)
                .build()
                .execute();

        return view;
    }


    private void handleWeatherPostExecute(final String response) {
        Log.e("handle weather", " post execute" );

        try {
            List<DailyWeather> dailyWeathers = new ArrayList<>();
            JSONObject result = new JSONObject(response);
            JSONArray listArray = result.getJSONArray("list");
            for (int i = 0; i < listArray.length(); i ++) {
                JSONObject jsonWeather = listArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0);
                JSONObject jsonTemp = listArray.getJSONObject(i).getJSONObject("main");
//                Log.e("jsonweather", " "+ jsonWeather);
//                Log.e("jsontemp", " " + jsonTemp);
                double max = convertKtoF(jsonTemp.getInt("temp_max"));
                double min = convertKtoF(jsonTemp.getInt("temp_min"));

                dailyWeathers.add(new DailyWeather.Builder(
                        jsonWeather.getString("main"))
                        .addIcon(jsonWeather.getString("icon"))
                        .addHighTemp(max)
                        .addLowTemp(min)
                        .build());
                //Log.e("daily weather: ", " " + max + " " + min + " " + jsonWeather.getString("main") + " "+ dailyWeathers);
            }
            mDailyWeather = dailyWeathers;

            //set recycle view
            Context context = getView().getContext();
            RecyclerView recyclerView = (RecyclerView) getView();
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyDailyWeatherRecyclerViewAdapter(mDailyWeather, mListener));

            onWaitFragmentInteractionHide();
//            loadFragment(fragment);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private float convertKtoF(double k) {
        return  Math.round((k*1.8 - 459.67) * 100) / 100;
    }

    private void handleErrorsInTask(String result) {
        Log.e("ASYNC_TASK_ERROR", result);
    }

    public void onWaitFragmentInteractionShow() {
        getActivity()
        .getSupportFragmentManager()
        .beginTransaction()
        .add(R.id.weather_home_container_3, new WaitFragment(), "WAIT")
        .addToBackStack(null)
        .commit();
    }
    public void onWaitFragmentInteractionHide() {
        getActivity()
        .getSupportFragmentManager()
        .beginTransaction()
        .remove(getActivity().getSupportFragmentManager().findFragmentByTag("WAIT"))
        .commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener extends WaitFragment.OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onWeatherListFragmentInteraction(DailyWeather item);
    }
}
