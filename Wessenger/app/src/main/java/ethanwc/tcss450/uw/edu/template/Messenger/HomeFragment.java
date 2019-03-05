package ethanwc.tcss450.uw.edu.template.Messenger;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ethanwc.tcss450.uw.edu.template.Connections.GetAsyncTask;
import ethanwc.tcss450.uw.edu.template.Connections.SendPostAsyncTask;
import ethanwc.tcss450.uw.edu.template.Main.WaitFragment;
import ethanwc.tcss450.uw.edu.template.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnHomeFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class HomeFragment extends Fragment {



    private OnHomeFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        FragmentManager manager= getChildFragmentManager();//create an instance of fragment manager


        FragmentTransaction transaction= manager.beginTransaction();//create an instance of Fragment-transaction

        transaction.add(R.id.My_Container_1_ID, new CurrentWeather(), "Frag_Top_tag");
        transaction.add(R.id.My_Container_2_ID, new ForecastWeather(), "Frag_Middle_tag");
        transaction.add(R.id.My_Container_3_ID, new ConversationFragment(), "Frag_Bottom_tag");


        transaction.commit();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHomeFragmentInteractionListener) {
            mListener = (OnHomeFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHomeFragmentInteractionListener");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnHomeFragmentInteractionListener {
        // TODO: Update argument type and name
        void onHomeFragmentInteraction(Uri uri);
    }

    public static class CurrentWeather extends Fragment {

        private OnCurrentWeatherUpdateListener mListener;


        private ImageView mWeatherImage;
        private Map<String, String> idCodes;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_weather_current, container, false);


            return view;
        }


        /**
         * OnAttach used to check whether the correct listeners have been implemented.
         *
         * @param context Context of the current ui situation.
         */
        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            if (context instanceof OnCurrentWeatherUpdateListener) {
                mListener = (OnCurrentWeatherUpdateListener) context;
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement OnCurrentWeatherUpdateListener");
            }
        }

        /**
         * Internal interface used to handle successful registration and login button.
         */
        public interface OnCurrentWeatherUpdateListener {
            void onWaitFragmentInteractionShow();
            void onWaitFragmentInteractionHide();
        }

        //makes calls to set stuff.
        //get weather info...set weather info
        @Override
        public void onStart() {
            super.onStart();

            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath("api.openweathermap.org/data/2.5/weather?zip=98332,us&appid=49e6beaf0af9b35f66b67a9c09e696aa")
//                .appendPath(getString(R.string.ep_userdetail))
                    .build();

            new GetAsyncTask.Builder("http://api.openweathermap.org/data/2.5/weather?zip=98332,us&appid=49e6beaf0af9b35f66b67a9c09e696aa")
                    .onPreExecute(this::handleLoginOnPre)
                    .onPostExecute(this::handleSetWeather)
                    .build()
                    .execute();



        }


        private void handleLoginOnPre() {
            mListener.onWaitFragmentInteractionShow();
        }

        public void handleSetWeather(final String response) {
            mListener.onWaitFragmentInteractionHide();

            try {
                JSONObject result = new JSONObject(response);

//                Log.e("WEATHERSTUFF", "res: " + result.getJSONArray("weather").get(0));





                if (result.has("weather")) {

                    JSONObject weather = new JSONObject(result.getJSONArray("weather").get(0).toString());

                    JSONObject maininfo = result.getJSONObject("main");
                    JSONObject wind = result.getJSONObject("wind");

                    String iconcode = weather.get("icon").toString();
                    String main = weather.get("main").toString();
                    String windspeed = wind.get("speed").toString();
                    String temperature = maininfo.get("temp").toString();
                    String pressure = maininfo.get("pressure").toString();

                    ((TextView)getView().findViewById(R.id.fragment_weather_current_main)).setText(main);

                    ((TextView)getView().findViewById(R.id.fragment_weather_current_temperature)).setText(temperature);
                    ((TextView)getView().findViewById(R.id.fragment_weather_current_pressure)).setText(pressure);
                    ((TextView)getView().findViewById(R.id.fragment_weather_current_wind_speed)).setText(windspeed);


                    String icon = "http://openweathermap.org/img/w/" +iconcode + ".png";

                    Picasso.get().load(icon).into((ImageView) getView().findViewById(R.id.fragment_weather_current_image));



                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        public void showWaitProgress () {

        }


    }

    public static class ForecastWeather extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_weather_forecast, container, false);

            return view;
        }
    }

    public static class RecentMessages extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_messages_recent, container, false);

            return view;
        }
    }
}