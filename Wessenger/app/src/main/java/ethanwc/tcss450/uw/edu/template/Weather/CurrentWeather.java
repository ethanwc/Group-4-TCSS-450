package ethanwc.tcss450.uw.edu.template.Weather;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;
import ethanwc.tcss450.uw.edu.template.Connections.GetAsyncTask;
import ethanwc.tcss450.uw.edu.template.Connections.SendPostAsyncTask;
import ethanwc.tcss450.uw.edu.template.Main.WaitFragment;
import ethanwc.tcss450.uw.edu.template.R;

/**
 * The class to display the currentweather of the saved zip code
 */
public class CurrentWeather extends Fragment {

    private OnCurrentWeatherUpdateListener mListener;
    private String mCity;
    private String mState;
    private int mZip;
    private ImageView mWeatherImage;
    private Map<String, String> idCodes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_weather_current, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Home Page");

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
        mZip = getArguments().getInt("zip");


        FloatingActionButton fab = getActivity().findViewById(R.id.fab_messaging_fab);
        fab.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Uri uri = new Uri.Builder()
                        .scheme("https")
                        .appendPath(getString(R.string.ep_getCityState))
                        .appendPath(Integer.toString(mZip))
                        .build();




                new GetAsyncTask.Builder(uri.toString())
                        .onPreExecute(this::onWaitFragmentInteractionShow)
                        .onPostExecute(this::handleGetCityStateOnPostExecute)
                        .onCancelled(this::handleErrorsInTask)
                        .build().execute();



            }
            /**
             * Handle errors that may occur during the AsyncTask.
             *
             * @param result the error message provide from the AsyncTask
             */
            private void handleErrorsInTask(String result) {
                Log.e("ASYNC_TASK_ERROR", result);
            }

            private void onWaitFragmentInteractionShow() {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.activity_messaging_container, new WaitFragment(), "WAIT")
                        .addToBackStack(null)
                        .commit();
            }

            /**
             * Helper method used to hide the wait fragment.
             */
            public void onWaitFragmentInteractionHide() {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .remove(getActivity().getSupportFragmentManager().findFragmentByTag("WAIT"))
                        .commit();
            }

            /**
             * Method to handle the result after adding location
             * @param s
             */
            private void handleAddLocationOnPostExecute(String s) {

                try {
                    JSONObject json = new JSONObject(s);
                    boolean success = json.getBoolean("success");
                    if (success) {
                        Log.e("LOCATION", "ADDED!");

                    } else {
                        Log.e("LOCATION", "NOT ADDED!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("LOCATION", "NOT ADDED!");
                }
                onWaitFragmentInteractionHide();


            }

            /**
             * Method to get the city and state name after the post execution
             * @param s
             */
            private void handleGetCityStateOnPostExecute(String s) {
                try {
                    JSONObject json = new JSONObject(s);
                    mCity = json.getString("city");
                    mState = json.getString("state");



                    String msg = getActivity().getIntent().getExtras().getString("email");
                    Uri uri = new Uri.Builder()
                            .scheme("https")
                            .appendPath(getString(R.string.ep_base_url))
                            .appendPath(getString(R.string.ep_addLocation))
                            .build();



                    json = new JSONObject();
                    try {

                        json.put("email", msg);
                        json.put("longitude", "0");
                        json.put("latitude", "0");
                        json.put("nickname", mCity);
                        json.put("zip", mZip);
//                        System.out.println("----------city---------"+mCity);
//                        System.out.println("----------city---------");
                    Log.e("CITY!!!!", mCity);
                    } catch (JSONException e) {
                        Log.wtf("CREDENTIALS", "Error creating JSON: " + e.getMessage());
                    }

                    new SendPostAsyncTask.Builder(uri.toString(), json)
                            .onPreExecute(this::onWaitFragmentInteractionShow)
                            .onPostExecute(this::handleAddLocationOnPostExecute)
                            .onCancelled(this::handleErrorsInTask)
                            .build().execute();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                onWaitFragmentInteractionHide();
            }
        });


        new GetAsyncTask.Builder("http://api.openweathermap.org/data/2.5/weather?zip=" + mZip + ",us&appid=49e6beaf0af9b35f66b67a9c09e696aa")
                .onPreExecute(this::handleLoginOnPre)
                .onPostExecute(this::handleSetWeather)
                .build()
                .execute();



    }

    /**
     * method to handle the pre login action of getasynctask
     */
    private void handleLoginOnPre() {
        mListener.onWaitFragmentInteractionShow();
    }

    /**
     * Method to handle the response of set weather async task
     * @param response
     */
    public void handleSetWeather(final String response) {
        mListener.onWaitFragmentInteractionHide();

        try {
            JSONObject result = new JSONObject(response);


            if (result.has("weather")) {

                JSONObject weather = new JSONObject(result.getJSONArray("weather").get(0).toString());

                JSONObject maininfo = result.getJSONObject("main");
                JSONObject wind = result.getJSONObject("wind");

                String iconcode = weather.get("icon").toString();
                String main = weather.get("main").toString();
                String temperature = maininfo.get("temp").toString();
                String min = maininfo.get("temp_min").toString();
                String max = maininfo.get("temp_max").toString();


//                ((TextView)getView().findViewById(R.id.fragment_weather_current_city)).setText(mCity);
                ((TextView)getView().findViewById(R.id.fragment_weather_current_main)).setText(main);
                ((TextView)getView().findViewById(R.id.fragment_weather_current_temperature)).setText(kelvinToFar(temperature) + "\u2109");
                ((TextView)getView().findViewById(R.id.fragment_weather_current_high_low)).setText(kelvinToFar(max) + "\u00b0" + "/" + kelvinToFar(min) + "\u00b0");


                String icon = "http://openweathermap.org/img/w/" +iconcode + ".png";
                Picasso.get().load(icon).into((ImageView) getView().findViewById(R.id.fragment_weather_current_image));


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    /**
     * Show wait progress
     */
    public void showWaitProgress () {

    }

    /**
     * Method to convert kelvin into fahrenheit
     * @param kelvin
     * @return fahrenheit
     */
    private int kelvinToFar(String kelvin) {
        return (int) (((Double.parseDouble(kelvin)) - 273.15) * 9/5 + 32);

    }

}