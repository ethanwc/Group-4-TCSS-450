package ethanwc.tcss450.uw.edu.template.Weather;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.model.HourlyWeather;


import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link HourlyWeather} and makes a call to the
 */
public class MyHourlyWeatherRecyclerViewAdapter extends RecyclerView.Adapter<MyHourlyWeatherRecyclerViewAdapter.ViewHolder> {

    private final List<HourlyWeather> mValues;

    public MyHourlyWeatherRecyclerViewAdapter(List<HourlyWeather> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_hourlyweather, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
//        holder.mItem = mValues.get(position);
        holder.mTemperatureView.setText(mValues.get(position).getTemp()+ "F");
        holder.mWeatherView.setText(mValues.get(position).getWeather());
        holder.mTime.setText(mValues.get(position).getTime());
        Log.e("time ", " " + mValues.get(position).getTime());

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTemperatureView;
        public final TextView mWeatherView;
        public final TextView mTime;
        public HourlyWeather mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mWeatherView = (TextView) view.findViewById(R.id.textView_hourlyweather_weather);
            mTemperatureView = (TextView) view.findViewById(R.id.textview_hourlyweather_temp);
            mTime = (TextView) view.findViewById(R.id.textview_hourlyweather_time);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTemperatureView.getText() + "'";
        }
    }
}
