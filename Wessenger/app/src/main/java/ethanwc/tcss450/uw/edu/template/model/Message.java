package ethanwc.tcss450.uw.edu.template.model;

import java.io.Serializable;

/**
 * Clas to encapsulate a message.
 */
public class Message implements Serializable {
    private final String mUsers;
    private final String mMessage;



    /**
     * Helper class for building message.
     */
    public static class Builder {
        private final String mMessage;
        private String mUsers;

        /**
         * Constructs a new builder.
         * @param message String used to represent the text of the message.
         */
        public Builder(String message) {
            mMessage = message;

        }

        public Builder addUsers(String users) {
            mUsers = users;
            return this;
        }
        /**
         * Method used to build the connection.
         * @return
         */
        public Message build() { return new Message(this);}

    }

    /**
     * Private constructor for builder to utilize.
     * @param builder Builder to be applied to Connection.
     */
    private Message(final Builder builder) {
        this.mMessage = builder.mMessage;
        this.mUsers = builder.mUsers;

    }

    /**
     * Helper method to return the users associated with the message.
     * @return String used to represent the users.
     */
    public String getUsers() {
        return mUsers;
    }

    /**
     * Helper method to return the text of the message.
     * @return String used to represent the text of the message.
     */
    public String getMessage() {
        return mMessage;
    }


}
