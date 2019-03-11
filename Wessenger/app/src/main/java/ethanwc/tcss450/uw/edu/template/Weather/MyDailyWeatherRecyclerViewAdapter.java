package ethanwc.tcss450.uw.edu.template.Weather;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.Weather.DailyWeatherFragment.OnListFragmentInteractionListener;
import ethanwc.tcss450.uw.edu.template.model.DailyWeather;
import java.util.Calendar;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DailyWeather} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyDailyWeatherRecyclerViewAdapter extends RecyclerView.Adapter<MyDailyWeatherRecyclerViewAdapter.ViewHolder> {

    private final List<DailyWeather> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyDailyWeatherRecyclerViewAdapter(List<DailyWeather> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_dailyweather, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mDate.setText(convertDate(position));
        holder.mTemperature.setText(mValues.get(position).getTemperature());
        holder.mWeatherMain.setText(mValues.get(position).getWeather());
        String iconcode = mValues.get(position).getIcon();
        String icon = "http://openweathermap.org/img/w/" +iconcode + ".png";

        Picasso.get().load(icon)
                .into((ImageView) holder.mIcon, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

        Log.e("weather", " "+ mValues.get(position).getWeather());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onWeatherListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    private String convertDate (int position) {
        int date = Calendar.DAY_OF_WEEK + position;
        String result;
        if ( date % 7 == 0) {
            result = "Sunday";
        } else if (date % 7 == 1) {
            result = "Monday";
        } else if (date % 7 == 2) {
            result = "Tuesday";
        } else if (date % 7 ==3) {
            result = "Wednesday";
        } else if (date % 7== 4) {
            result = "Thursday";
        } else if (date % 7 == 5) {
            result = "Friday";
        } else {
            result = "Saturday";
        }
        return result;
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mWeatherMain;
        public final TextView mDate;
        public final TextView mTemperature;
        public final ImageView mIcon;
        public DailyWeather mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mWeatherMain = (TextView) view.findViewById(R.id.textview_weathermain);
            mDate = (TextView) view.findViewById(R.id.textView_dayofweek);
            mTemperature = (TextView) view.findViewById(R.id.textview_temperature);
            mIcon = (ImageView) view.findViewById(R.id.imageView_weathericon);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mWeatherMain.getText() + "'";
        }
    }
}
