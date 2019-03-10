package ethanwc.tcss450.uw.edu.template.model;

import java.io.Serializable;

public class HourlyWeather implements Serializable {

    private final String mLocation;
    private final String mMessage;
    private final String mChatid;



    /**
     * Helper class for building message.
     */
    public static class Builder {
        private final String mMessage;
        private String mLocation;
        private String mChatid;

        /**
         * Constructs a new builder.
         * @param message String used to represent the text of the message.
         */
        public Builder(String message) {
            mMessage = message;
        }

        public HourlyWeather.Builder addLocation(String location) {
            mLocation = location;
            return this;
        }
        public HourlyWeather.Builder setChatID(String chatid) {
            mChatid = chatid;
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
        this.mMessage = builder.mMessage;
        this.mLocation = builder.mLocation;
        this.mChatid = builder.mChatid;

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
    public String getMessage() {
        return mMessage;
    }


    /**
     * Helper method to return the chat id of the message.
     * @return String used to represent the chat id of the message.
     */
    public String getChatid() {
        return mChatid;
    }
}
