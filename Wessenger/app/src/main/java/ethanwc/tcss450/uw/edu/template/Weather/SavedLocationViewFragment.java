package ethanwc.tcss450.uw.edu.template.Weather;

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
import android.widget.TextView;

import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.utils.PushReceiver;
import me.pushy.sdk.Pushy;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SavedLocationViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SavedLocationViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavedLocationViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private PushMessageReceiver mPushMessageReciever;
//    private MenuItem mMenuItem;


    @Override
    public void onResume() {
        super.onResume();
//        System.out.println("in push message receive---->On Resume");
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
//            getActivity().unregisterReceiver(mPushMessageReciever);
        }
    }
    public SavedLocationViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SavedLocationViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SavedLocationViewFragment newInstance(String param1, String param2) {
        SavedLocationViewFragment fragment = new SavedLocationViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * OnStart used to populate the textview fields with the information in the arguments.
     */
    @Override
    public void onStart() {
        super.onStart();
        if(getArguments() != null) {

            String nickname = getArguments().getString("nickname");
            String latitude = getArguments().getString("latitude");
            String longitude = getArguments().getString("longitude");
            String zip = getArguments().getString("zip");
            System.out.println("--onstart---"+ nickname);
            TextView tView = getActivity().findViewById(R.id.txtview_locationview_nickname);
            tView.setText(nickname);

            tView = getActivity().findViewById(R.id.txtview_locationview_latitude);
            tView.setText("Latitude : "+latitude);

            tView = getActivity().findViewById(R.id.txtview_locationview_longitude);
            tView.setText("longitude : "+longitude);

            tView = getActivity().findViewById(R.id.txtview_locationview_zip);
            tView.setText("zip : "+zip);

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_location_view, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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

            System.out.println("in push message receive---+++++->SavedLocationViewFragment"+intent.toString());
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
