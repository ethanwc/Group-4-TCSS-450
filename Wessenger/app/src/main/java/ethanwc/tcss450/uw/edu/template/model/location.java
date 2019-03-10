package ethanwc.tcss450.uw.edu.template.model;
import java.io.Serializable;

public class location  implements Serializable, Comparable{
    private final String mNickname;
    private final String mCity;
    private final String mState;
    private final String mZip;


    @Override
    public int compareTo(Object o) {
        location loc = (location) o;
        return mZip.compareTo(loc.mZip);
    }

    /**
     * Helper class for building connection.
     */
    public static class Builder {
        private String mNickname = "";
        private String mCity = "";
        private String mState = "";
        private String mZip = "";

        public Builder(String nickname) {
            mNickname = nickname;

        }


        /**
         * Helper method used to add a first name to a connection.
         * @param val String used to represent the first name of the connection.
         * @return Builder.
         */
        public Builder addNickname(final String val) {
            mNickname = val;
            return this;
        }

        //        /**
//         * Helper method used to add a last name to a connection.
//         * @param val String used to represent the last name of the connection.
//         * @return Builder.
//         */
        public Builder addCity(final String val) {
            mCity = val;
            return this;
        }

        //        /**
//         * Helper method used to add a username to a connection.
//         * @param val String used to represent the username of the connection.
//         * @return Builder.
//         */
        public Builder addState(final String val) {
            mState = val;
            return this;
        }
        public Builder addzip(final String val) {
            mZip = val;
            return this;
        }

        /**
         * Method used to build the connection.
         * @return
         */
        public location build() { return new location(this);}

    }

    /**
     * Private constructor for builder to utilize.
     * @param builder Builder to be applied to Connection.
     */
    private location(final Builder builder) {
        this.mNickname = builder.mNickname;
        this.mCity = builder.mCity;
        this.mState = builder.mState;
        this.mZip = builder.mZip;
    }

    /**
     * Helper method to return the first name of the connection.
     * @return String used to represent the first name of the connection.
     */
    public String getNickname() {
        return mNickname;
    }

    //    /**
//     * Helper method to return the last name of the connection.
//     * @return String used to represent the last name of the connection.
//     */
    public String getCity() {
        return mCity;
    }
    //
//    /**
//     * Helper method to return the username of the connection.
//     * @return String used to represent the username of the connection.
//     */
    public String getState() {
        return mState;
    }
    //
//    /**
//     * Helper method to return the email of the connection.
//     * @return String used to represent the email of the connection.
//     */
    public String getZip() {
        return mZip;
    }
}

