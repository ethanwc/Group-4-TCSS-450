package ethanwc.tcss450.uw.edu.template.Weather;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import ethanwc.tcss450.uw.edu.template.R;

public class CurrentWeather extends Fragment {

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