package ethanwc.tcss450.uw.edu.template.utils;

public class ChatModel {

    public static final int TEXT_TYPE = 0;
    public static final int IMAGE_TYPE = 1;

    public int type;
    public int data;
    public String sender;
    public String text;

    /**
     *  Constructor
     * @param type
     * @param text
     * @param data
     * @param sender
     */
    public ChatModel(int type, String text, int data, String sender)
    {
        this.type = type;
        this.data = data;
        this.text = text;
        this.sender = sender;
    }
}