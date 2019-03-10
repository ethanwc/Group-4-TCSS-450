package ethanwc.tcss450.uw.edu.template.model;

import java.io.Serializable;

public class DailyWeather implements Serializable {
    private final String mLocation;
    private final String mWeatherMain;
    private final double mMax;
    private final double mMin;
    private final String mTemperature;
    private final String mIcon;



    /**
     * Helper class for building message.
     */
    public static class Builder {
        private final String mWeatherMain;
        private String mLocation;
        private double mMax;
        private double mMin;
        private String mIcon;

        /**
         * Constructs a new builder.
         * @param main String used to represent the text of the message.
         */
        public Builder(String main) {
            mWeatherMain = main;
        }

        public DailyWeather.Builder addLocation(String location) {
            mLocation = location;
            return this;
        }
        public DailyWeather.Builder addHighTemp(double highTemp) {
            mMax = highTemp;
            return this;
        }

        public DailyWeather.Builder addLowTemp(double lowTemp) {
            mMin = lowTemp;
            return this;
        }

        public DailyWeather.Builder addIcon(String iconCode){
            mIcon = iconCode;
            return this;
        }

        /**
         * Method used to build the connection.
         * @return
         */
        public DailyWeather build() { return new DailyWeather(this);}

    }

    /**
     * Private constructor for builder to utilize.
     * @param builder Builder to be applied to Connection.
     */
    private DailyWeather(final DailyWeather.Builder builder) {
        this.mWeatherMain = builder.mWeatherMain;
        this.mLocation = builder.mLocation;
        this.mMax = builder.mMax;
        this.mMin = builder.mMin;
        this.mTemperature = "H:" + mMax + "/ L:" + mMin;
        this.mIcon = builder.mIcon;

    }

    /**
     * Helper method to return the users associated with the message.
     * @return String used to represent the users.
     */
    public String getLocation() {
        return mLocation;
    }

    /**
     * Helper method to return the text of the message.
     * @return String used to represent the text of the message.
     */
    public String getWeather() {
        return mWeatherMain;
    }


    /**
     * Helper method to return the max temperature for the day.
     * @return String used to represent the chat id of the message.
     */
    public double getMax() {
        return mMax;
    }

    public double getMin() {return  mMin;}

    public String getTemperature() {return mTemperature;}

    public String getIcon() {return  mIcon;}
}
