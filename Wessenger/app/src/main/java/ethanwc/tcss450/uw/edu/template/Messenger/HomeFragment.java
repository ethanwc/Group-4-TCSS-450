package ethanwc.tcss450.uw.edu.template.Messenger;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.Weather.CurrentWeather;
import ethanwc.tcss450.uw.edu.template.temp.ChatFragment2;
import ethanwc.tcss450.uw.edu.template.utils.PushReceiver;
import me.pushy.sdk.Pushy;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class HomeFragment extends Fragment {




    /**
     * Constructor
     */
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    /**
     * Set the view to be of the current weather, the 10 day forecast, and recent conversations.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        FragmentManager manager= getChildFragmentManager();//create an instance of fragment manager


        FragmentTransaction transaction= manager.beginTransaction();//create an instance of Fragment-transaction

        //pass zip to currentWeather
        Bundle args = new Bundle();
        args.putSerializable("zip", getArguments().getInt("zip"));
        Fragment currentWeather = new CurrentWeather();
        currentWeather.setArguments(args);
        Fragment connectionsF = new ConnectionsFragment();
        Bundle args2 = new Bundle();

        args2.putSerializable(ConnectionsFragment.ARG_CONNECTION_LIST,
                getArguments().getSerializable(ConnectionsFragment.ARG_CONNECTION_LIST));
        connectionsF.setArguments(args2);


        transaction.add(R.id.messaging_home_container_1, currentWeather, "Frag_Top_tag");
        transaction.add(R.id.messaging_home_container_2, connectionsF, "Frag_Middle_tag");


        transaction.commit();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }




    @Override
    public void onDetach() {
        super.onDetach();
    }


    /**
     * Get's the 10-day forecast
     */
    public static class ForecastWeather extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_weather_forecast, container, false);

            return view;
        }
    }

    /**
     * Shows recent messages, within the last week.
     */
    public static class RecentMessages extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_messages_recent, container, false);

            return view;
        }
    }
    private PushMessageReceiver mPushMessageReciever;
    @Override
    public void onResume() {
        super.onResume();
        System.out.println("in push message receive---->On Resume");
        if (mPushMessageReciever == null) {
            mPushMessageReciever = new PushMessageReceiver();
        }

        IntentFilter iFilter = new IntentFilter( PushReceiver.RECEIVED_NEW_MESSAGE);
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
     * In app notification
     */
    public void changeColorOnMsg(){

        Spannable text = new SpannableString(((AppCompatActivity) getActivity()).getSupportActionBar().getTitle());
        text.setSpan(new ForegroundColorSpan( Color.RED), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
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
    /**
     * In app notification
     */
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
     * In app notification
     */
    public void changeColorOnAddToChat(){

        Spannable text = new SpannableString(((AppCompatActivity) getActivity()).getSupportActionBar().getTitle());
        text.setSpan(new ForegroundColorSpan(Color.RED), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(text);


        NavigationView navigationView = (NavigationView) ((AppCompatActivity) getActivity()).findViewById(R.id.navview_messanging_nav);
        if(navigationView!= null){
            Menu menu = navigationView.getMenu();

            MenuItem item = menu.findItem(R.id.nav_chat_home);
            SpannableString s = new SpannableString(item.getTitle());
            s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
            item.setTitle(s);

        }

    }
    /**
     * A BroadcastReceiver that listens for messages sent from PushReceiver
     */
    private class PushMessageReceiver extends BroadcastReceiver {
        private static final String CHANNEL_ID = "1";
        @Override
        public void onReceive(Context context, Intent intent) {

            System.out.println("in push message receive---+++++->Home Fragment"+intent.toString());
            if(intent.hasExtra("SENDER") && intent.hasExtra("MESSAGE")) {

                String type = intent.getStringExtra("TYPE");
                String sender = intent.getStringExtra("SENDER");
                String messageText = intent.getStringExtra("MESSAGE");
                String msgtype = intent.getStringExtra( "MsgType" );
                String receiver = intent.getStringExtra( "Receiver" );
                String chatid = intent.getStringExtra( "chatid" );
                String chatName = intent.getStringExtra( "chatName" );
                System.out.println(chatid+"  chatid ---and--- chatName  "+chatName);


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
                    if(chatid.equals( "1" )) {
                        changeColorOnMsg();
                    }else{
                        changeColorOnAddToChat();
                    }
                    if(msgtype.equals( "0" )){
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                                .setAutoCancel(true)
                                .setSmallIcon(R.drawable.ic_message_black_24dp)
                                .setContentTitle("Message from: " + sender)
                                .setContentText(chatName+" : "+messageText)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                        // Automatically configure a Notification Channel for devices running Android O+
                        Pushy.setNotificationChannel(builder, context);

                        // Get an instance of the NotificationManager service
                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

                        // Build the notification and display it
                        notificationManager.notify(1, builder.build());
                    }else {
                        NotificationCompat.Builder builder = new NotificationCompat.Builder( context, CHANNEL_ID )
                                .setAutoCancel( true )
                                .setSmallIcon( R.drawable.ic_message_black_24dp )
                                .setContentTitle( "Message from: " + sender )
                                .setContentText("Picture Message : " +messageText )
                                .setPriority( NotificationCompat.PRIORITY_DEFAULT );

                        // Automatically configure a Notification Channel for devices running Android O+
                        Pushy.setNotificationChannel( builder, context );

                        // Get an instance of the NotificationManager service
                        NotificationManager notificationManager = (NotificationManager) context.getSystemService( context.NOTIFICATION_SERVICE );

                        // Build the notification and display it
                        notificationManager.notify( 1, builder.build() );
                    }

                }else if(type.equals("acpt")){
                    changeColorOnInv();
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                            .setAutoCancel(true)
                            .setSmallIcon(R.drawable.ic_person_black_24dp)
                            .setContentTitle("Connection Request Accepted : " + receiver)
                            .setContentText(messageText)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    // Automatically configure a Notification Channel for devices running Android O+
                    Pushy.setNotificationChannel(builder, context);

                    // Get an instance of the NotificationManager service
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

                    // Build the notification and display it
                    notificationManager.notify(1, builder.build());
                } else if(type.equals( "addchat" )){
                    System.out.println("  ---------------------- chatName  "+chatName);
                    changeColorOnAddToChat();
                    NotificationCompat.Builder builder = new NotificationCompat.Builder( context, CHANNEL_ID )
                            .setAutoCancel( true )
                            .setSmallIcon( R.drawable.ic_message_black_24dp )
                            .setContentTitle( "You have been added to the group "  )
                            .setContentText(" "+chatName  )
                            .setPriority( NotificationCompat.PRIORITY_DEFAULT );

                    // Automatically configure a Notification Channel for devices running Android O+
                    Pushy.setNotificationChannel( builder, context );

                    // Get an instance of the NotificationManager service
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService( context.NOTIFICATION_SERVICE );

                    // Build the notification and display it
                    notificationManager.notify( 1, builder.build() );
                }
            }
        }
    }
}
