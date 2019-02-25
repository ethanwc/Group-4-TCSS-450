package ethanwc.tcss450.uw.edu.template.Messenger;


import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.utils.PushReceiver;
import me.pushy.sdk.Pushy;

/**
 * A simple {@link Fragment} subclass.
 */
public class InvReqFragment extends Fragment {
    private PushMessageReceiver mPushMessageReciever;

    public InvReqFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inv_req, container, false);
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
                    System.out.println("No notification in invitaion----->");

//                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
//                            .setAutoCancel(true)
//                            .setSmallIcon(R.drawable.ic_person_black_24dp)
//                            .setContentTitle("New Contact Request from : " + sender)
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


                } else if(type.equals("msg")) {
//                    System.out.println("type is=>>>"+type);
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

