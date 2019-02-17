package ethanwc.tcss450.uw.edu.template.model;

import java.io.Serializable;

/**
 * Clas to encapsulate a connection.
 */
public class Connection implements Serializable {
    private final String mFirst;
    private final String mLast;
    private final String mUsername;
    private final String mEmail;

    /**
     * Helper class for building connection.
     */
    public static class Builder {
        private String mFirst = "";
        private String mLast = "";
        private String mUsername = "";
        private final String mEmail;

        /**
         * Constructs a new builder.
         * @param email String used to represent the email of the connection.
         */
        public Builder(String email) {
            mEmail = email;

        }

        /**
         * Helper method used to add a first name to a connection.
         * @param val String used to represent the first name of the connection.
         * @return Builder.
         */
        public Builder addFirst(final String val) {
            mFirst = val;
            return this;
        }

        /**
         * Helper method used to add a last name to a connection.
         * @param val String used to represent the last name of the connection.
         * @return Builder.
         */
        public Builder addLast(final String val) {
            mLast = val;
            return this;
        }

        /**
         * Helper method used to add a username to a connection.
         * @param val String used to represent the username of the connection.
         * @return Builder.
         */
        public Builder addUsername(final String val) {
            mUsername = val;
            return this;
        }
        /**
         * Method used to build the connection.
         * @return
         */
        public Connection build() { return new Connection(this);}

    }

    /**
     * Private constructor for builder to utilize.
     * @param builder Builder to be applied to Connection.
     */
    private Connection(final Builder builder) {
        this.mFirst = builder.mFirst;
        this.mLast = builder.mLast;
        this.mUsername = builder.mUsername;
        this.mEmail = builder.mEmail;
    }

    /**
     * Helper method to return the first name of the connection.
     * @return String used to represent the first name of the connection.
     */
    public String getFirst() {
        return mFirst;
    }

    /**
     * Helper method to return the last name of the connection.
     * @return String used to represent the last name of the connection.
     */
    public String getLast() {
        return mLast;
    }

    /**
     * Helper method to return the username of the connection.
     * @return String used to represent the username of the connection.
     */
    public String getUsername() {
        return mUsername;
    }

    /**
     * Helper method to return the email of the connection.
     * @return String used to represent the email of the connection.
     */
    public String getEmail() {
        return mEmail;
    }
}
