package ethanwc.tcss450.uw.edu.template.Messenger;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.dummy.DummyContent;
import ethanwc.tcss450.uw.edu.template.dummy.DummyContent.DummyItem;
import ethanwc.tcss450.uw.edu.template.model.Message;
import ethanwc.tcss450.uw.edu.template.utils.PushReceiver;
import me.pushy.sdk.Pushy;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnMessageListFragmentInteractionListener}
 * interface.
 */
public class ConversationFragment extends Fragment {

    public static final String ARG_MESSAGE_LIST = "messages list";
    private List<Message> mMessages;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnMessageListFragmentInteractionListener mListener;
    private PushMessageReceiver mPushMessageReciever;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ConversationFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ConversationFragment newInstance(int columnCount) {
        ConversationFragment fragment = new ConversationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mMessages = new ArrayList<Message> (Arrays.asList((Message[]) getArguments().getSerializable(ARG_MESSAGE_LIST)));
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
        View view = inflater.inflate(R.layout.fragment_conversation_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            recyclerView.setAdapter(new MyConversationRecyclerViewAdapter(mMessages, mListener));
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
        if (context instanceof OnMessageListFragmentInteractionListener) {
            mListener = (OnMessageListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMessageListFragmentInteractionListener");
        }
    }

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
    public interface OnMessageListFragmentInteractionListener {
        void onMessageListFragmentInteraction(Message item);
    }
    /**
     * A BroadcastReceiver that listens for messages sent from PushReceiver
     */
    private class PushMessageReceiver extends BroadcastReceiver {
        private static final String CHANNEL_ID = "1";
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("in push message receive---+++++->MainActivity---."+intent.toString());
            if(intent.hasExtra("SENDER") && intent.hasExtra("MESSAGE")) {
                String type = intent.getStringExtra("TYPE");
                String sender = intent.getStringExtra("SENDER");
                String messageText = intent.getStringExtra("MESSAGE");
                System.out.println("The message is: " + messageText);
                if (type.equals("inv")) {
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


                } else if(type.equals("msg")) {
                    System.out.println("From ConversationFragment--->");
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
