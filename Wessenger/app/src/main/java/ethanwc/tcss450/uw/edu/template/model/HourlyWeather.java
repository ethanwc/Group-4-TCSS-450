package ethanwc.tcss450.uw.edu.template.model;

import java.io.Serializable;

public class HourlyWeather implements Serializable {

    private final String mLocation;
    private final String mWeather;
    private final double mTemp;



    /**
     * Helper class for building message.
     */
    public static class Builder {
        private final String mWeather;
        private String mLocation;
        private double mTemp;

        /**
         * Constructs a new builder.
         * @param weather String used to represent the text of the message.
         */
        public Builder(String weather) {
            mWeather = weather;
        }

        public HourlyWeather.Builder addLocation(String location) {
            mLocation = location;
            return this;
        }
        public HourlyWeather.Builder setTemp(double temp) {
            mTemp = temp;
            return this;
        }

        /**
         * Method used to build the connection.
         * @return
         */
        public HourlyWeather build() { return new HourlyWeather(this);}

    }

    /**
     * Private constructor for builder to utilize.
     * @param builder Builder to be applied to Connection.
     */
    private HourlyWeather(final HourlyWeather.Builder builder) {
        this.mWeather = builder.mWeather;
        this.mLocation = builder.mLocation;
        this.mTemp = builder.mTemp;

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
        return mWeather;
    }


    /**
     * Helper method to return the chat id of the message.
     * @return String used to represent the chat id of the message.
     */
    public double getTemp() {
        return mTemp;
    }
}
