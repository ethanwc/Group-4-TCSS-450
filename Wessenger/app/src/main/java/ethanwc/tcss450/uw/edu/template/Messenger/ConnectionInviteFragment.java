package ethanwc.tcss450.uw.edu.template.Messenger;


import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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
import ethanwc.tcss450.uw.edu.template.utils.PushReceiver;
import me.pushy.sdk.Pushy;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectionInviteFragment extends Fragment {
    private PushMessageReceiver mPushMessageReciever;

    public ConnectionInviteFragment() {
        // Required empty public constructor
    }

    /**
     * OnStart used to grab email and jwt token from arguments and set url string.
     */
    @Override
    public void onStart() {
        super.onStart();
        // change color of title
        Spannable text = new SpannableString(((AppCompatActivity) getActivity()).getSupportActionBar().getTitle());
        text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.messageText)), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(text);

        // change color of menuitem
        NavigationView navigationView = (NavigationView) ((AppCompatActivity) getActivity()).findViewById(R.id.navview_messanging_nav);
        if (navigationView != null) {
            Menu menu = navigationView.getMenu();

            MenuItem item = menu.findItem(R.id.nav_chat_view_connections);
            SpannableString s = new SpannableString(item.getTitle());
            s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.messageText)), 0, s.length(), 0);
            item.setTitle(s);

        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connection_invite, container, false);
    }
    @Override
    public void onResume() {
        super.onResume();
//        System.out.println("in push message receive---->On Resume");
        if (mPushMessageReciever == null) {
            mPushMessageReciever = new PushMessageReceiver();
        }
//        System.out.println("from weather");
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
    private class PushMessageReceiver extends BroadcastReceiver {
        private static final String CHANNEL_ID = "1";
        @Override
        public void onReceive(Context context, Intent intent) {

            System.out.println("in push message receive---+++++->ConnectionInviteFragment"+intent.toString());
            if(intent.hasExtra("SENDER") && intent.hasExtra("MESSAGE")) {

                String type = intent.getStringExtra("TYPE");
                String sender = intent.getStringExtra("SENDER");
                String messageText = intent.getStringExtra("MESSAGE");

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
                    changeColorOnMsg();
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                            .setAutoCancel(true)
                            .setSmallIcon(R.drawable.ic_message_black_24dp)
                            .setContentTitle("Message from: " + sender)
                            .setContentText(messageText)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    // Automatically configure a Notification Channel for devices running Android O+
                    Pushy.setNotificationChannel(builder, context);

                    // Get an instance of the NotificationManager service
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

                    // Build the notification and display it
                    notificationManager.notify(1, builder.build());
                }
            }
        }
    }
}

