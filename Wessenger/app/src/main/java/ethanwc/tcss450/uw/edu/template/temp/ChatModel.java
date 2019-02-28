package ethanwc.tcss450.uw.edu.template.temp;

public class ChatModel {

    public static final int TEXT_TYPE = 0;
    public static final int IMAGE_TYPE = 1;

    public int type;
    public int data;
    public String text;

    public ChatModel(int type, String text, int data)
    {
        this.type = type;
        this.data = data;
        this.text = text;
    }
}