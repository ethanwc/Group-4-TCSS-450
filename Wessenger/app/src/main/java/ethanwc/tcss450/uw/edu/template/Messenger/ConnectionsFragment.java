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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import ethanwc.tcss450.uw.edu.template.utils.PushReceiver;
import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.model.Connection;
import me.pushy.sdk.Pushy;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnConnectionListFragmentInteractionListener}
 * interface.
 */
public class ConnectionsFragment extends Fragment {

    public static final String ARG_CONNECTION_LIST = "connections list";
    private List<Connection> mConnections;

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnConnectionListFragmentInteractionListener mListener;
    private PushMessageReceiver mPushMessageReciever;
    private String mMyEmail;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ConnectionsFragment() {
    }

    /**
     * Store your connection list
     * @param columnCount
     * @return fragment
     */
    public static ConnectionsFragment newInstance(int columnCount) {
        ConnectionsFragment fragment = new ConnectionsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * OnCreate used to populate the list of connections.
     * @param savedInstanceState Bundle.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mConnections = new ArrayList<Connection> (Arrays.asList((Connection[]) Objects.requireNonNull(getArguments().getSerializable(ARG_CONNECTION_LIST))));
            mMyEmail = getArguments().getString("passEmail");
            System.out.println(mMyEmail+"----- in connections");
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
        View view = inflater.inflate(R.layout.fragment_connections_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyConnectionsRecyclerViewAdapter(mConnections, mListener));
        }


        return view;
    }

    /**
     * OnAttach used to check whether the correct listeners have been implemented.
     * @param context Context of the current ui situation.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnConnectionListFragmentInteractionListener) {
            mListener = (OnConnectionListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }
//public boolean onCreateOptions
    /**
     * OnDetach used to remove the listener.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    @SuppressWarnings("EmptyMethod")
    public interface OnConnectionListFragmentInteractionListener {
        void onConnectionListFragmentInteraction(Connection item);
        void onConnectionListDeleteFragmentInteraction(Connection item);
    }

    public void refreshFragment(){
        System.out.println("#############");
        NavigationView navigationView = (NavigationView) ((AppCompatActivity) getActivity()).findViewById(R.id.navview_messanging_nav);
        if(navigationView!= null){
            Menu menu = navigationView.getMenu();

            MenuItem item = menu.findItem(R.id.nav_chat_view_connections);
//            ((MessagingHomeActivity)getActivity()).onNavigationItemSelected(item);

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        System.out.println("in push message receive---->On Connection Fragment Ignored");
//        if (mPushMessageReciever == null) {
//            mPushMessageReciever = new PushMessageReceiver();
//        }
//
//        IntentFilter iFilter = new IntentFilter( PushReceiver.RECEIVED_NEW_MESSAGE);
//        getActivity().registerReceiver(mPushMessageReciever, iFilter);
    }
    /**
     * OnPause handles push notifications.
     */
    @Override
    public void onPause() {
//        System.out.println("in push message receive---->On Pause");
        super.onPause();
//        if (mPushMessageReciever != null){
//            getActivity().unregisterReceiver(mPushMessageReciever);
//        }
    }
    /**
     * In app notification
     */
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

            System.out.println("in push message receive---+++++->ConnectionsFragment"+intent.toString());
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
//                    String msgtype = intent.getStringExtra( "MsgType" );
                    if(chatid.equals( "1" )) {
                        changeColorOnMsg();
                    }else{
                        changeColorOnAddToChat();
                    }
                    //


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


                    //
                }else if(type.equals("acpt")){
                    refreshFragment();
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