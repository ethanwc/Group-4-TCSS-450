package ethanwc.tcss450.uw.edu.template.Messenger;


import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ethanwc.tcss450.uw.edu.template.Connections.SendPostAsyncTask;
import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.utils.PushReceiver;
import me.pushy.sdk.Pushy;

/**
 * Fragment used to represent an individual chat window.
 */
public class ChatFragment extends Fragment {

    private static final String TAG = "CHAT_FRAG";
    private TextView mMessageOutputTextView;
    private EditText mMessageInputEditText;
    private String mEmail;
    private String mJwToken;
    private String mSendUrl;
    private String mChatID;
    private PushMessageReceiver mPushMessageReciever;

    /**
     * Required empty public constructor.
     */
    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * OnResume handles push notifications.
     */
    @Override
    public void onResume() {
        super.onResume();

        NavigationView navigationView = (NavigationView) ((AppCompatActivity) getActivity()).findViewById(R.id.navview_messanging_nav);
        MenuItem menuItem = (MenuItem) navigationView.findViewById(R.id.nav_global_chat);
//        menuItem.setTitle()

        System.out.println("chat fragment ---->On Resume");
        if (mPushMessageReciever == null) {
            mPushMessageReciever = new PushMessageReceiver();
        }
        IntentFilter iFilter = new IntentFilter(PushReceiver.RECEIVED_NEW_MESSAGE);
        getActivity().registerReceiver(mPushMessageReciever, iFilter);
    }

    /**
     * OnPause handles push notifications.
     */
    @Override
    public void onPause() {
//        System.out.println("in push message receive---->On Pause");
        super.onPause();
        if (mPushMessageReciever != null){
            getActivity().unregisterReceiver(mPushMessageReciever);
        }
    }

    /**
     * OnCreate used to remove the options menu.
     * @param savedInstanceState
     */
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    /**
     * OnCreateView used to instantiate relevant items to the fragment.
     *
     * @param inflater LayoutInflater used to inflate the layout for the fragment.
     * @param container ViewGroup used as a container to hold the items in the fragment.
     * @param savedInstanceState bundle.
     * @return inflated fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootLayout = inflater.inflate(R.layout.fragment_chat, container, false);
        mMessageOutputTextView = rootLayout.findViewById(R.id.text_chat_message_display);
        mMessageOutputTextView.setMovementMethod(new ScrollingMovementMethod());
        mMessageInputEditText = rootLayout.findViewById(R.id.edit_chat_message_input);
        rootLayout.findViewById(R.id.button_send_message).setOnClickListener(this::handleSendClick);

        return rootLayout;
    }

    /**
     * OnStart used to grab email and jwt token from arguments and set url string.
     */
    @Override
    public void onStart() {
        super.onStart();

        Spannable text = new SpannableString(((AppCompatActivity) getActivity()).getSupportActionBar().getTitle());
        text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.messageText)), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(text);

        NavigationView navigationView = (NavigationView) ((AppCompatActivity) getActivity()).findViewById(R.id.navview_messanging_nav);
        if(navigationView!= null){
            Menu menu = navigationView.getMenu();

            MenuItem item = menu.findItem(R.id.nav_global_chat);
            SpannableString s = new SpannableString(item.getTitle());
            s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.messageText)), 0, s.length(), 0);
            item.setTitle(s);

        }
        //Store arguments in variables.
        if (getArguments() != null) {
            mEmail = getArguments().getString("email_token_123");
            mJwToken = getArguments().getString("jwt_token");
            mChatID = getArguments().getString("chat_id");
        }

        //Store url in variable.
        mSendUrl = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_messaging_base))
                .appendPath(getString(R.string.ep_messaging_send))
                .build()
                .toString();

        setChatHistory();
    }

    /**
     * Helper method used to handle sending a message.
     * @param theButton Button that was clicked to send message.
     */
    private void handleSendClick(final View theButton) {
        String msg = mMessageInputEditText.getText().toString();
        JSONObject messageJson = new JSONObject();
        //Build message for web service.
        try {
            messageJson.put("email", mEmail);
            messageJson.put("message", msg);
            messageJson.put("chatId", mChatID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Send message to web service.
        new SendPostAsyncTask.Builder(mSendUrl, messageJson)
                .onPostExecute(this::endOfSendMsgTask)
                .onCancelled(error -> Log.e(TAG, error))
                .addHeaderField("authorization", mJwToken)
                .build().execute();
    }

    /**
     * Helper method used to clear the input field.
     * @param result JSON object returned from the web service.
     */
    private void endOfSendMsgTask(final String result) {
        try {
            //This is the result from the web service
            JSONObject res = new JSONObject(result);
            if(res.has("success") && res.getBoolean("success")) {
                //The web service got our message. Time to clear out the input EditText
                mMessageInputEditText.setText("");
                //its up to you to decide if you want to send the message to the output here
                //or wait for the message to come back from the web service.
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setChatHistory() {
        String getAll = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_messaging_base))
                .appendPath(getString(R.string.ep_messaging_getAll))
                .build()
                .toString();
        JSONObject messageJson = new JSONObject();
        //Build message for web service.
        try {
            messageJson.put("chatId", mChatID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new SendPostAsyncTask.Builder(getAll, messageJson)
                .onPostExecute(this::getAllHistory)
                .onCancelled(error -> Log.e(TAG, error))
                .addHeaderField("authorization", mJwToken)
                .build().execute();
    }


    //output the chat history result
    private void getAllHistory(final String result) {
        try {
            //This is the result from the web service
            JSONObject res = new JSONObject(result);
            if(res.has("messages")) {

                JSONArray chatHistoryArray = res.getJSONArray("messages");
                String chatHistoryText = "";
                //Log.e("history: ", "  " + res.get("messages"));
                for (int i = chatHistoryArray.length() -1; i >= 0 ; i--) {

//                    if (chatHistoryArray.getString(i).substring(0, emailLength - 1).equals(email)) {
//                        System.out.println("GOD POOPING ON MORTALS");
//                    }
                    chatHistoryText += chatHistoryArray.getString(i) + "\n";
                }
                mMessageOutputTextView.setText(chatHistoryText);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void changeColorOnMsg(){

        Spannable text = new SpannableString(((AppCompatActivity) getActivity()).getSupportActionBar().getTitle());
        text.setSpan(new ForegroundColorSpan(Color.RED), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(text);


        NavigationView navigationView = (NavigationView) ((AppCompatActivity) getActivity()).findViewById(R.id.navview_messanging_nav);
        if(navigationView!= null){
            Menu menu = navigationView.getMenu();

            MenuItem item = menu.findItem(R.id.nav_global_chat);
            SpannableString s = new SpannableString(item.getTitle());
            s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
            item.setTitle(s);

        }

    }
    public void changeColorOnInv(){

        Spannable text = new SpannableString(((AppCompatActivity) getActivity()).getSupportActionBar().getTitle());
        text.setSpan(new ForegroundColorSpan(Color.RED), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(text);

        NavigationView navigationView = (NavigationView) ((AppCompatActivity) getActivity()).findViewById(R.id.navview_messanging_nav);
        if(navigationView!= null){
            Menu menu = navigationView.getMenu();

            MenuItem item = menu.findItem(R.id.nav_chat_view_connections);
            SpannableString s = new SpannableString(item.getTitle());
            s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
            item.setTitle(s);

        }

    }

    /**
     * A BroadcastReceiver that listens for messages sent from PushReceiver
     */
    private class PushMessageReceiver extends BroadcastReceiver  {
        private static final String CHANNEL_ID = "1";
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("in push message receive---+++++->chat fragment"+intent.toString());
            if(intent.hasExtra("SENDER") && intent.hasExtra("MESSAGE")) {

                String type = intent.getStringExtra("TYPE");
                String sender = intent.getStringExtra("SENDER");
                String messageText = intent.getStringExtra("MESSAGE");
                mMessageOutputTextView.append(sender + ":" + messageText);
                mMessageOutputTextView.append(System.lineSeparator());
                mMessageOutputTextView.append(System.lineSeparator());

                if (type.equals("inv")) {
                    changeColorOnInv();
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                            .setAutoCancel(true)
                            .setSmallIcon(R.drawable.ic_person_black_24dp)
                            .setContentTitle("New Contact Request from : " + sender)
                            .setContentText(messageText)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    // Automatically configure a Notification Channel for devices running Android O+
                    Pushy.setNotificationChannel(builder, context);

                    // Get an instance of the NotificationManager service
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

                    // Build the notification and display it
                    notificationManager.notify(1, builder.build());


                }else if(type.equals("msg")) {
//                    changeColorOnMsg();
//                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
//                            .setAutoCancel(true)
//                            .setSmallIcon(R.drawable.ic_message_black_24dp)
//                            .setContentTitle("Message from: " + sender)
//                            .setContentText(messageText)
//                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//                    // Automatically configure a Notification Channel for devices running Android O+
//                    Pushy.setNotificationChannel(builder, context);
//
//                    // Get an instance of the NotificationManager service
//                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
//
//                    // Build the notification and display it
//                    notificationManager.notify(1, builder.build());
                }
            }
        }
    }


}
