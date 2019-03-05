package ethanwc.tcss450.uw.edu.template.Messenger;


import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;

import ethanwc.tcss450.uw.edu.template.Main.LoginFragment;
import ethanwc.tcss450.uw.edu.template.Main.WaitFragment;
import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.model.Credentials;
import ethanwc.tcss450.uw.edu.template.utils.PushReceiver;
import me.pushy.sdk.Pushy;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddContactFragment extends WaitFragment {
    private OnNewContactFragmentButtonAction mListener;
    private EditText mEmail;
    private PushMessageReceiver mPushMessageReciever;
private String mMyEmail;

    public AddContactFragment() {
        // Required empty public constructor
    }

    /**
     * OnCreate used to populate the list of connections.
     * @param savedInstanceState Bundle.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            mMyEmail = getArguments().getString("passEmail");
            System.out.println(mMyEmail+"----- in Addcontact");
        }


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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_contact, container, false);
    }

    /**
     * OnAttach used to check whether the correct listeners have been implemented.
     * @param context Context of the current ui situation.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddContactFragment.OnNewContactFragmentButtonAction) {
            mListener = (AddContactFragment.OnNewContactFragmentButtonAction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHomeFragmentInteractionListener");
        }
    }

    /**
     * OnStart used to populate the textview fields with the information in the arguments.
     */
    @Override
    public void onStart() {
        super.onStart();

        mEmail = getActivity().findViewById(R.id.edittext_newcontact_email);

        Button addButton = getActivity().findViewById(R.id.button_newcontact_add);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mEmail.getText().toString().isEmpty() || !mEmail.getText().toString().contains("@")) {
                    mEmail.setError("Please enter a valid email.");
                } else {
                    mListener.addContactButton(new Credentials.Builder(mEmail.getText().toString())
                            .addEmail2(mEmail.getText().toString())
                            .addVerify(0).build());

                }
            }
        });



    }


    public interface OnNewContactFragmentButtonAction extends WaitFragment.OnFragmentInteractionListener{

        void addContactButton(Credentials credentials);


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

            System.out.println("in push message receive---+++++->Add Contact Fragment"+intent.toString());
            if(intent.hasExtra("SENDER") && intent.hasExtra("MESSAGE")) {

                String type = intent.getStringExtra("TYPE");
                String sender = intent.getStringExtra("SENDER");
                String messageText = intent.getStringExtra("MESSAGE");
                String msgtype = intent.getStringExtra( "MsgType" );

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
                    if(msgtype.equals( "0" )){
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
                }
            }
        }
    }
}
