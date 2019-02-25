package ethanwc.tcss450.uw.edu.template.Messenger;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ethanwc.tcss450.uw.edu.template.Connections.SendPostAsyncTask;
import ethanwc.tcss450.uw.edu.template.Main.MainActivity;
import ethanwc.tcss450.uw.edu.template.Main.WaitFragment;
import ethanwc.tcss450.uw.edu.template.Messenger.AddContactFragment.OnNewContactFragmentButtonAction;
import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.Weather.ChangeLocationsFragment;
import ethanwc.tcss450.uw.edu.template.Weather.SavedLocationFragment;
import ethanwc.tcss450.uw.edu.template.Weather.WeatherHome;
import ethanwc.tcss450.uw.edu.template.dummy.DummyContent;
import ethanwc.tcss450.uw.edu.template.model.Connection;
import ethanwc.tcss450.uw.edu.template.model.Credentials;
import ethanwc.tcss450.uw.edu.template.model.Message;
import me.pushy.sdk.Pushy;

/**
 * Acivity class which manages functionality beyond login/registration.
 */
public class MessagingHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NewMessageFragment.OnSendBtnNewMessage,
        ConversationFragment.OnMessageListFragmentInteractionListener, ConnectionsFragment.OnConnectionListFragmentInteractionListener,
        InvitationsFragment.OnInvitationListFragmentInteractionListener,
        ChangePasswordFragment.OnChangePasswordFragmentInteractionListener, SavedLocationFragment.OnListFragmentInteractionListener, OnNewContactFragmentButtonAction, WeatherHome.OnFragmentInteractionListener {


    private Bundle mArgs;
    private FloatingActionButton mFab;
    private ArrayList<String> mEmailList;
    private ArrayList<String> mEmails;
    private ArrayList<String> mFirsts;
    private ArrayList<String> mLasts;
    private ArrayList<String> mUNames;
    private ArrayList<String> mChats;
    private Map<String, String> mPeople;
    private int mChatCount = 0;
    Multimap<String, String> mChatMembers;
    Map<Integer, String> mChatUsernames;
    private ArrayList<Connection> mConnections;
    private int mCounter = 0;
    DrawerLayout mdrawer;

    private PushMessageReceiver mPushMessageReciever;
//    private MenuItem mMenuItem;

    private static final String[] COUNTRIES = new String[]{"Belgium",
            "France", "France_", "Italy", "Germany", "Spain"};
    @Override
    public void onResume() {
        super.onResume();
        System.out.println("=>>>><<<<<-====");
        if (mPushMessageReciever == null) {
            mPushMessageReciever = new PushMessageReceiver();
        }

        System.out.println("ole");
//        IntentFilter iFilter = new IntentFilter(PushReceiver.RECEIVED_NEW_MESSAGE);
//        getActivity().registerReceiver(mPushMessageReciever, iFilter);
    }
    /**
     * OnPause handles push notifications.
     */
    @Override
    public void onPause() {
//        System.out.println("in push message receive---->On Pause");
        super.onPause();
        System.out.println("ole");
        if (mPushMessageReciever != null){
//            getActivity().unregisterReceiver(mPushMessageReciever);
        }
    }


    /**
     * OnCreate used to instantiate the starting state of the application.
     *
     * @param savedInstanceState Bundle of instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging_home);
        Toolbar toolbar = findViewById(R.id.toolbar_messenging_toolbar);
        setSupportActionBar(toolbar);

        loadChats();
        //Hide the FAB upon main activity loading.
        mFab = findViewById(R.id.fab_messaging_fab);
        mFab.setEnabled(false);
        mFab.hide();


        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
//System.out.println("+=======from pushy");
            mEmailList = new ArrayList<String>();
            mEmailList = getIntent().getStringArrayListExtra("a");

        }

        //Set on click listener for FAB
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new AddContactFragment());
                mFab.hide();
                mFab.setEnabled(false);
            }

            /**
             * Load the desired fragment.
             * @param frag
             */
//            private void loadFragment(Fragment frag) {
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_messaging_container, frag)
//                        .addToBackStack(null);
//                transaction.commit();
//            }
        });

        //Setup the navigation
        mdrawer = findViewById(R.id.activity_messaging_container);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mdrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mdrawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navview_messanging_nav);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment;
        if (getIntent().getBooleanExtra(getString(R.string.keys_intent_notification_msg), false)) {
            fragment = new ChatFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_messaging_container, fragment)
                    .commit();
        } else if (getIntent().getBooleanExtra(getString(R.string.keys_intent_notification_invitation), false)) {
            fragment = new InvitationsFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_messaging_container, fragment)
                    .commit();
        } else {
            loadChats();
        }
    }

    /**
     * OnBackPressed used to handle minimizing of the navigation drawer and closing the FAB on inappropriate windows.
     */
//    @Override
//    public void onBackPressed() {
//        View connectionViewFrag = findViewById(R.id.fragment_messaging_connectionView);
//        View addcontactViewFrag = findViewById(R.id.fragment_messenger_addcontact);
//        @SuppressWarnings("RedundantCast") DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_messaging_container);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//
//
//        } else if (connectionViewFrag != null || addcontactViewFrag != null) {
//            //Show the FAB on correct windows when back is pressed.
//            mFab.show();
//            mFab.setEnabled(true);
//            super.onBackPressed();
//        } else {
//            //Hide the FAB on correct windows when back is pressed
//            mFab.hide();
//            mFab.setEnabled(false);
//            super.onBackPressed();
//        }
//        getSupportFragmentManager().addT
//    }

    /**
     * OnCreateOptionsMenu used to help create the options menu.
     *
     * @param menu Encompassing menu.
     * @return Boolean used to represent if the menu has been created.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//    mMenu = menu;
        // Inflate the search menu action bar.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.messaging_home, menu);


        // Get the search menu.
        MenuItem searchMenu = menu.findItem(R.id.app_bar_menu_search);

        // Get SearchView object.
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenu);

        // Get SearchView autocomplete object.
        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setBackgroundColor(Color.BLUE);
        searchAutoComplete.setTextColor(Color.GREEN);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.holo_blue_light);
        String dataArr[] = new String[mEmailList.size()];
        for (int i = 0; i < mEmailList.size(); i++) {
            dataArr[i] = mEmailList.get(i);
        }

        ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, dataArr);
        searchAutoComplete.setAdapter(newsAdapter);

        // Listen to search view item on click event.
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                String queryString = (String) adapterView.getItemAtPosition(itemIndex);
                searchAutoComplete.setText("" + queryString);
                ConnectionViewFragment connectionViewFrag;
                connectionViewFrag = new ConnectionViewFragment();

                Bundle args = new Bundle();

                args.putSerializable("email", queryString);

                FloatingActionButton connectionsFab = findViewById(R.id.fab_messaging_fab);
                connectionsFab.setEnabled(false);
                connectionsFab.hide();

                connectionViewFrag.setArguments(args);
                loadFragment(connectionViewFrag);
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Method used handle when menu items are selected.
     *
     * @param item MenuItem used to represent the item that has been selected.
     * @return Boolean used to represent whether the item has been selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_change_password) {

            ChangePasswordFragment changePasswordFragment
                    = new ChangePasswordFragment();
            mArgs = getIntent().getExtras();
            changePasswordFragment.setArguments(mArgs);
            getSupportActionBar().setTitle("Change Password");
            loadFragment(changePasswordFragment);

        } else if (id == R.id.action_logout) {

            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void messageIn(){



        mdrawer.closeDrawers();
//        drawer.closeDrawers();
//        System.out.println(mMenu.size());
//        System.out.println( mMenu.findItem(R.id.nav_global_chat).toString());
//        Toast toast = Toast.makeText(getApplicationContext(),
//                "This is a message displayed in a Toast",
//                Toast.LENGTH_SHORT);

//        toast.show();
//        MenuItem mMenuItem = mMenu.findItem(R.id.nav_global_chat);

//        MenuItem items = item;
//            items.setTitle("title changed");
//        SpannableString s = new SpannableString("Global Chat");
//        s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
//        mMenuItem.setTitle(s);

    }

    /**
     * Method used to handle when menu items are selected.
     *
     * @param item MenuItem used to represent the item that has been selected.
     * @return Boolean used to represent whether the item has been selected.
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        mMenuItem = R.id.nav_global_chat
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //Chat has been chosen
        if (id == R.id.nav_global_chat) {

            String jwt = getIntent().getExtras().getString(getString(R.string.keys_intent_jwt));
            String email = getIntent().getExtras().getString(("email"));
            Bundle args = new Bundle();
            args.putString("jwt_token", jwt);
            args.putString("email_token_123", email);
            args.putString("chat_id", "1");
            Fragment chatFrag = new ChatFragment();
            chatFrag.setArguments(args);
            getSupportActionBar().setTitle("Global chat");
//--set  color
//            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_global_chat);
//
////
////
//            Menu menu = navigationView.getMenu();
//            MenuItem message = menu.findItem(id);
//
//            System.out.println(message);

            loadFragment(chatFrag);
        }
        //Weather home has been chosen
        if (id == R.id.nav_weather_home) {

            WeatherHome weatherHome = new WeatherHome();
            getSupportActionBar().setTitle("Weather Home");
            mFab.hide();
            mFab.setEnabled(false);
            loadFragment(weatherHome);
            //Change locations has been chosen
        } else if (id == R.id.nav_Change_Locations) {
            ChangeLocationsFragment changeLocationsFragment = new ChangeLocationsFragment();
            getSupportActionBar().setTitle("Change Location");
            mFab.hide();
            mFab.setEnabled(false);
            loadFragment(changeLocationsFragment);
            //Saved locations has been chosen
        } else if (id == R.id.nav_View_Saved_Location) {
            SavedLocationFragment locationFragment = new SavedLocationFragment();
            getSupportActionBar().setTitle("Saved Location");
            mFab.hide();
            mFab.setEnabled(false);

            loadFragment(locationFragment);
            //Messenger home has been chosen
        } else if (id == R.id.nav_chat_home) {
            loadChats();
            //Connections has been chosen
        } else if (id == R.id.nav_chat_view_connections) {
System.out.println("===========");
            mEmails = new ArrayList<>();
            mFirsts = new ArrayList<>();
            mLasts = new ArrayList<>();
            mUNames = new ArrayList<>();
            mConnections = new ArrayList<>();
            //Build ASNC task to grab connections from web service.
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_getContacts))
                  .build();
            //handleConnectionGetInfoOnPostExecute
            String msg = getIntent().getExtras().getString("email");
            Credentials creds = new Credentials.Builder(msg).build();
            getSupportActionBar().setTitle("Connections-");
            new SendPostAsyncTask.Builder(uri.toString(),creds.asJSONObject())
                    .onPreExecute(this::onWaitFragmentInteractionShow)
                    .onPostExecute(this::handleConnectionGetOnPostExecute)
                    .onCancelled(this::handleErrorsInTask)
                    .build().execute();
            //Show FAB

            mFab.setEnabled(true);
            mFab.show();

        //Requests/Invitations has been chosen
            //Requests/Invitations has been chosen
        } else if (id == R.id.nav_Request_Invitations) {

            mEmails = new ArrayList<>();

//            Uri uri = new Uri.Builder()
//                    .scheme("https")
//                    .appendPath(getString(R.string.ep_base_url))
//                    .appendPath(getString(R.string.ep_getrequests))
//                    .build();
//            String msg = getIntent().getExtras().getString("email");
//            Credentials creds = new Credentials.Builder(msg).build();
//            getSupportActionBar().setTitle("Connections");
//            new SendPostAsyncTask.Builder(uri.toString(),creds.asJSONObject())
//                    .onPreExecute(this::onWaitFragmentInteractionShow)
//                    .onPostExecute(this::handleRequestGetOnPostExecute)
//                    .onCancelled(this::handleErrorsInTask)
//                    .build().execute();



//
//
//            Uri uri2 = new Uri.Builder()
//                    .scheme("https")
//                    .appendPath(getString(R.string.ep_base_url))
//                    .appendPath(getString(R.string.ep_getinvitations))
//                    .build();
//            String msg2 = getIntent().getExtras().getString("email");
//            Credentials creds2 = new Credentials.Builder(msg2).build();
//            getSupportActionBar().setTitle("Connections");
//            new SendPostAsyncTask.Builder(uri2.toString(),creds2.asJSONObject())
//                    .onPreExecute(this::onWaitFragmentInteractionShow)
//                    .onPostExecute(this::handleInvitationGetOnPostExecute)
//                    .onCancelled(this::handleErrorsInTask)
//                    .build().execute();
//
//            InvitationsFragment invitationsFragment = new InvitationsFragment();
//            RequestsFragment requestsFragment = new RequestsFragment();
//            InvReqFragment invReqFragment = new InvReqFragment();
//            getSupportActionBar().setTitle("Requests/Invitations");
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragment_messaging_container, invReqFragment)
//                    .replace(R.id.fragment_messaging_inv_container, invitationsFragment)
//                    .replace(R.id.fragment_messaging_req_container, requestsFragment)
//                    .addToBackStack(null);
//            transaction.commit();
            Uri uri2 = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_getinvitations))
                    .build();
            String msg2 = getIntent().getExtras().getString("email");
            Credentials creds2 = new Credentials.Builder(msg2).build();
            getSupportActionBar().setTitle("Invitation/Request");
            new SendPostAsyncTask.Builder(uri2.toString(),creds2.asJSONObject())
                    .onPreExecute(this::onWaitFragmentInteractionShow)
                    .onPostExecute(this::handleInvitationGetOnPostExecute)
                    .onCancelled(this::handleErrorsInTask)
                    .build().execute();
        }

        DrawerLayout drawer = findViewById(R.id.activity_messaging_container);
        drawer.closeDrawer(GravityCompat.START);


        return true;
    }

//    private void handleRequestGetOnPostExecute(String result) {
//
//        //parse JSON
//        try {
//            mEmails = new ArrayList<>();
//            mConnections = new ArrayList<>();
//            JSONObject resultJSON = new JSONObject(result);
//            boolean success = resultJSON.getBoolean("success");
//            JSONArray data = resultJSON.getJSONArray("message");
//            onWaitFragmentInteractionHide();
//            if (success) {
//                onWaitFragmentInteractionHide();
//                for (int j = 0; j < data.length(); j++) {
//                    mEmails.add(data.getString(j));
//                }
//                //When done parsing begin creating list of connections
//                for (int i = 0; i < mEmails.size(); i++) {
//                    Connection conn = new Connection.Builder(mEmails.get(i)).build();
//                    mConnections.add(conn);
//                }
//
//
//                //Bundle list of connections as arguments and load connection fragment
//                Connection[] connectionsAsArray = new Connection[mConnections.size()];
//                connectionsAsArray = mConnections.toArray(connectionsAsArray);
//                //Bundle connections and send as arguments
//                Bundle args = new Bundle();
//
//                Fragment frag = new InvReqFragment();
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_messaging_container, frag );
//                transaction.commit();
//                args.putSerializable(RequestsFragment.ARG_REQUEST_LIST, connectionsAsArray);
//                frag = new RequestsFragment();
//                frag.setArguments(args);
//                transaction = getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_messaging_req_container, frag );
//                transaction.commit();
//
//
//                Log.e("duper!!!!", "yup");
//
//
//            }
//            onWaitFragmentInteractionHide();
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Log.e("SUPER!!", e.getMessage());
//            //notify user
//            onWaitFragmentInteractionHide();
//        }
//
//    }


    /**
     * Method to logout of the app, and delete saved password information
     */

    private void logout() {
        new DeleteTokenAsyncTask().execute();
        SharedPreferences prefs =
                getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        //remove the saved credentials from StoredPrefs
        prefs.edit().remove(getString(R.string.keys_prefs_password)).apply();
        prefs.edit().remove(getString(R.string.keys_prefs_email)).apply();
        //close the app
        //finishAndRemoveTask();
        //or close this activity and bring back the Login
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        //Ends this Activity and removes it from the Activity back stack.
        finish();
    }


    /**
     * Handle errors that may occur during the AsyncTask.
     *
     * @param result the error message provide from the AsyncTask
     */
    private void handleErrorsInTask(String result) {
        Log.e("ASYNC_TASK_ERROR", result);
    }

    public void onWaitFragmentInteractionShow() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_messaging_container, new WaitFragment(), "WAIT")
                .addToBackStack(null)
                .commit();
    }

    /**
     * Helper method used to hide the wait fragment.
     */
    public void onWaitFragmentInteractionHide() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentByTag("WAIT"))
                .commit();
    }

    /**
     * Listener method.
     */
    @Override
    public void onSendFragmentInteraction() {
    }

    /**
     * Load the desire fragment
     *
     * @param frag
     */
    private void loadFragment(Fragment frag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_messaging_container, frag)
                .addToBackStack(null);
        transaction.commit();
    }


    /**
     * Method which listens for a connection being selected in the recycler view.
     *
     * @param theItem Connection which will represent a connection.
     */
    @Override
    public void onConnectionListFragmentInteraction(Connection theItem) {

        ConnectionViewFragment connectionViewFrag;
        connectionViewFrag = new ConnectionViewFragment();

        Bundle args = new Bundle();
        mFab.setEnabled(false);
        mFab.hide();
        args.putSerializable("username", theItem.getUsername());
        args.putSerializable("first", theItem.getFirst());
        args.putSerializable("last", theItem.getLast());
        args.putSerializable("email", theItem.getEmail());
        connectionViewFrag.setArguments(args);
        loadFragment(connectionViewFrag);

    }

    /**
     * Method which listens for a delete button click on the connections page
     *
     * @param item Connection which is to be deleted.
     */
    @Override
    public void onConnectionListDeleteFragmentInteraction(Connection item) {

        //Build ASNYC  task for deleteing the contact
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_deleteContact))
                .build();
        String msg = getIntent().getExtras().getString("email");
        String msg2 = item.getEmail();

        JSONObject json = new JSONObject();
        try {

            json.put("email", msg);
            json.put("email2", msg2);


        } catch (JSONException e) {
            Log.wtf("CREDENTIALS", "Error creating JSON: " + e.getMessage());
        }

        new SendPostAsyncTask.Builder(uri.toString(), json)
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleConnectionDeleteOnPostExecute)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();


        mFab.setEnabled(true);
        mFab.show();

    }


    /**
     * Method which listens for adding a contact button.
     *
     * @param credentials Credentials used to represent the user information going to be added as a contact.
     */
    @Override
    public void addContactButton(Credentials credentials) {

        //Build ASYNC task for adding contact
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_addContact))
                .build();
        String msg = getIntent().getExtras().getString("email");

        String msg2 = credentials.getEmail2();
        int verify = credentials.getVerify();
        JSONObject json = new JSONObject();
        try {

            json.put("email", msg);
            json.put("email2", msg2);
            json.put("verify", verify);

        } catch (JSONException e) {
            Log.wtf("CREDENTIALS", "Error creating JSON: " + e.getMessage());
        }
        new SendPostAsyncTask.Builder(uri.toString(), json)
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleConnectionAddOnPostExecute)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();


        mFab.setEnabled(true);
        mFab.show();

    }

    @Override
    public void onMessageListFragmentInteraction(Message item) {

        ChatFragment chatFrag;
        chatFrag = new ChatFragment();

        Bundle args = new Bundle();


//        mEmail = getArguments().getString("email_token_123");
//        mJwToken = getArguments().getString("jwt_token");
//        mChatID = getArguments().getString("chat_id");


        args.putSerializable("chat_id", item.getChatid());
        args.putSerializable("email_token_123", getIntent().getExtras().getString("email"));
        args.putSerializable("jwt_token", getIntent().getExtras().getString(getString(R.string.keys_intent_jwt)));
        mFab.setEnabled(false);
        mFab.hide();

        chatFrag.setArguments(args);
        loadFragment(chatFrag);
    }


    /**
     * Method listening for change password button to be clicked.
     */
    @Override
    public void onChangePasswordClicked() {
        getSupportActionBar().setTitle("Messaging");
        loadChats();
    }

    /**
     * Helper method used to handle the tasks after the async task has been completed for receiving contact list.
     *
     * @param result String which represents the JSON result.
     */
    private void handleConnectionGetOnPostExecute(final String result) {
        //parse JSON
        try {
            System.out.println("1------_>");
            mCounter=0;
            JSONObject resultJSON = new JSONObject(result);
            boolean success = resultJSON.getBoolean("success");
            JSONArray data = resultJSON.getJSONArray("message");

            if (success) {

                //Create list of connections
                for (int i = 0; i < data.length(); i++) {

                    String email = data.getString(i);
                    //Create list of connections later, make list of emails for now
                    mEmails.add(email);
                    //Build ASNC task to grab connections from web service.

                }
                for (int i = 0; i < mEmails.size(); i++) {
                    Uri uri = new Uri.Builder()
                            .scheme("https")
                            .appendPath(getString(R.string.ep_base_url))
                            .appendPath(getString(R.string.ep_memberinfo))
                            .build();
System.out.println("from handle connection get on post execute");
                    Credentials creds = new Credentials.Builder(mEmails.get(i)).build();
                    new SendPostAsyncTask.Builder(uri.toString(), creds.asJSONObject())
                            .onPostExecute(this::handleConnectionGetInfoOnPostExecute)
                            .onCancelled(this::handleErrorsInTask)
                            .build().execute();
                }
                //

                //
                onWaitFragmentInteractionHide();
            } else {
                //Not successful return from webservice
                onWaitFragmentInteractionHide();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("SUPER!!", e.getMessage());
            //notify user
            onWaitFragmentInteractionHide();
        }

    }


    /**
     * Makes a request, if wish is granted, makes stuff happen.
     */
    private void loadChats() {
        //web request to /getchats
        Uri getchats = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_getchats))
                .build();

        JSONObject id = new JSONObject();
        String email = getIntent().getExtras().getString("email");

        try {
            id.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new SendPostAsyncTask.Builder(getchats.toString(), id)
                .onPostExecute(this::handleGetChatsOnPost)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();

    }


    /**
     * Setup chats after they are loaded.
     */
    private Message[] setChatInfo() {

        Message[] m = new Message[mChats.size()];
        for (int i = 0; i < mChats.size(); i++) {
            StringBuilder members = new StringBuilder(256);
                List<String> peopleInChat = new ArrayList(mChatMembers.get(mChats.get(i)));
                for (String person: peopleInChat)
                    members.append(mPeople.get(person) + " ");

                m[i] = new Message.Builder("chat name?").addUsers(("" + members.toString())).setChatID(mChats.get(i)).build();
        }
        return m;
    }

    /**
     * Method to get all the chat's a user is in.
     * @param result Chats of the user, wow.
     */
    @SuppressLint("UseSparseArrays")
    private void handleGetChatsOnPost(final String result) {
        try {
            JSONObject resultJSON = new JSONObject(result);
            boolean success = resultJSON.getBoolean("success");
            JSONArray chatids = resultJSON.getJSONArray("chatid");
            JSONArray people = resultJSON.getJSONArray("people");
            JSONArray usernames = resultJSON.getJSONArray("username");


            if (success) {
                mChats = new ArrayList<>();
                mPeople = new HashMap<>();
                mChatMembers = ArrayListMultimap.create();

                for (int i = 0; i < chatids.length(); i ++)
                    mChats.add(chatids.get(i).toString());

                for (int i = 0; i < people.length(); i ++)
                    mChatMembers.put(people.getJSONObject(i).getString("id"), people.getJSONObject(i).getString("member"));

                for (int i = 0; i < usernames.length(); i ++)
                    if (!mPeople.containsKey(usernames.getJSONObject(i).getString("id")))
                        mPeople.put(usernames.getJSONObject(i).getString("id"), usernames.getJSONObject(i).getString("username"));


                Log.e("Output", mChats.toString());

                Log.e("Output", mChatMembers.toString());

                Log.e("Output", mPeople.toString());
                swapToChats();
            }


            else onWaitFragmentInteractionHide();

        } catch (JSONException e) {
            e.printStackTrace();
            onWaitFragmentInteractionHide();
        }
    }


    /*
    Just loads the chats fragment.
     */
    private void swapToChats() {
        Message[] m = setChatInfo();
        Bundle args = new Bundle();
        args.putSerializable(ConversationFragment.ARG_MESSAGE_LIST, m);
        Fragment frag = new ConversationFragment();
        frag.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_messaging_container, frag)
                .addToBackStack(null);
        transaction.commit();


        //Show FAB
        mFab.setEnabled(true);
        mFab.show();
    }

    /**
     * Method listening for contact details button to be pressed.
     *
     * @param result String representing the JSON result.
     */
    private void handleConnectionGetInfoOnPostExecute(final String result) {
        //parse JSON
        try {
            System.out.println("2------_>");
            JSONObject resultJSON = new JSONObject(result);
            boolean success = resultJSON.getBoolean("success");
            JSONArray data = resultJSON.getJSONArray("message");

            if (success) {
                //Grab contact info from JSON result
                mFirsts.add(data.getString(0));
                mLasts.add(data.getString(1));
                mUNames.add(data.getString(2));
                mCounter++;
                System.out.println("3------_>");

                //When done parsing begin creating list of connections
                if (mCounter == mEmails.size()) {
                    for (int i = 0; i < mEmails.size(); i++) {
                        Connection conn = new Connection.Builder(mEmails.get(i))
                                .addFirst(mFirsts.get(i)).addLast(mLasts.get(i))
                                .addUsername(mUNames.get(i)).build();
                        mConnections.add(conn);
                        System.out.println("from email loop--->"+i);
                    }

                    onWaitFragmentInteractionHide();
                    //Bundle list of connections as arguments and load connection fragment
                    Connection[] connectionsAsArray = new Connection[mConnections.size()];
                    connectionsAsArray = mConnections.toArray(connectionsAsArray);
                    //Bundle connections and send as arguments
                    Bundle args = new Bundle();
                    System.out.println("4------_>");
                    args.putSerializable(ConnectionsFragment.ARG_CONNECTION_LIST, connectionsAsArray);
                    Fragment frag = new ConnectionsFragment();
                    frag.setArguments(args);
                    System.out.println("connection ---------->");
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_messaging_container, frag)
                            .addToBackStack(null);
                    transaction.commit();

                }
            } else {
                //Not successful return from webservice
                onWaitFragmentInteractionHide();
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("SUPER!!", e.getMessage());
            //notify user
            onWaitFragmentInteractionHide();
        }

    }

    /**
     * Method used to handle the tasks after the ASYNC task returns for deleting a contact.
     *
     * @param result String which represents the JSON result.
     */
    private void handleConnectionDeleteOnPostExecute(final String result) {
        //parse JSON
        try {
            JSONObject resultJSON = new JSONObject(result);
            boolean success = resultJSON.getBoolean("success");

            if (success) {
                //Build ASYNC task to get the new contacts list.
                Uri uri = new Uri.Builder()
                        .scheme("https")
                        .appendPath(getString(R.string.ep_base_url))
                        .appendPath(getString(R.string.ep_getContacts))
                        .build();
                String msg = getIntent().getExtras().getString("email");
                Credentials creds = new Credentials.Builder(msg).build();
                getSupportActionBar().setTitle("Connections");
                new SendPostAsyncTask.Builder(uri.toString(), creds.asJSONObject())
                        .onPreExecute(this::onWaitFragmentInteractionShow)
                        .onPostExecute(this::handleConnectionGetOnPostExecute)
                        .onCancelled(this::handleErrorsInTask)
                        .build().execute();
                //Hide wait fragment to go on to next ASYNC call
                onWaitFragmentInteractionHide();
                //Not successful return from webservice
            } else {
                //Delete unesuccessful
                TextView delete = findViewById(R.id.textview_connections_details_deletebutton);
                delete.setError("Unexpected error.");
                mFab.hide();
                mFab.setEnabled(false);
                onWaitFragmentInteractionHide();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("SUPER!!", e.getMessage());
            //notify user
            mFab.hide();
            mFab.setEnabled(false);
            onWaitFragmentInteractionHide();
        }
    }

    /**
     * Method used to handle the tasks after the ASYNC task returns for adding a contact.
     *
     * @param result String which represents the JSON result.
     */
    private void handleConnectionAddOnPostExecute(final String result) {
        //parse JSON
        try {
            JSONObject resultJSON = new JSONObject(result);
            boolean success = resultJSON.getBoolean("success");

            if (success) {
                //Hide wait fragment for next ASYNC task
                //Build ASYNC task for getting new contact list
                Uri uri = new Uri.Builder()
                        .scheme("https")
                        .appendPath(getString(R.string.ep_base_url))
                        .appendPath(getString(R.string.ep_getContacts))
                        .build();
                String msg = getIntent().getExtras().getString("email");

                Credentials creds = new Credentials.Builder(msg).build();

                getSupportActionBar().setTitle("Connections");
                new SendPostAsyncTask.Builder(uri.toString(), creds.asJSONObject())
                        .onPreExecute(this::onWaitFragmentInteractionShow)
                        .onPostExecute(this::handleConnectionGetOnPostExecute)
                        .onCancelled(this::handleErrorsInTask)
                        .build().execute();
                onWaitFragmentInteractionHide();
                //Not successful return from webservice


            } else {
                //Add was unsuccessful, let user know the email is unavailable for adding.
                EditText email = findViewById(R.id.edittext_newcontact_email);
                email.setError(resultJSON.getString("message"));
                mFab.hide();
                mFab.setEnabled(false);
                onWaitFragmentInteractionHide();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("SUPER!!", e.getMessage());
            //notify user
            mFab.hide();
            mFab.setEnabled(false);
            onWaitFragmentInteractionHide();
        }

    }

    private void handleInvitationDeleteOnPostExecute(String result) {
        //parse JSON
        try {
            JSONObject resultJSON = new JSONObject(result);
            boolean success = resultJSON.getBoolean("success");

            if (success) {
                //Build ASYNC task to get the new contacts list.
                Uri uri = new Uri.Builder()
                        .scheme("https")
                        .appendPath(getString(R.string.ep_base_url))
                        .appendPath(getString(R.string.ep_getinvitations))
                        .build();
                String msg = getIntent().getExtras().getString("email");
                Credentials creds = new Credentials.Builder(msg).build();
                getSupportActionBar().setTitle("Invitations/Requests");
                new SendPostAsyncTask.Builder(uri.toString(),creds.asJSONObject())
                        .onPreExecute(this::onWaitFragmentInteractionShow)
                        .onPostExecute(this::handleInvitationGetOnPostExecute)
                        .onCancelled(this::handleErrorsInTask)
                        .build().execute();
                //Hide wait fragment to go on to next ASYNC call
                onWaitFragmentInteractionHide();
                //Not successful return from webservice
            } else {
                //Delete unesuccessful
                TextView delete = findViewById(R.id.textview_invitations_email);
                delete.setError("Unexpected error.");
                mFab.hide();
                mFab.setEnabled(false);
                onWaitFragmentInteractionHide();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("SUPER!!", e.getMessage());
            //notify user
            mFab.hide();
            mFab.setEnabled(false);
            onWaitFragmentInteractionHide();
        }
    }



    private void handleInvitationGetOnPostExecute(String result) {

        //parse JSON
        try {
            mEmails = new ArrayList<>();
            mConnections = new ArrayList<>();
            JSONObject resultJSON = new JSONObject(result);
            boolean success = resultJSON.getBoolean("success");
            JSONArray data = resultJSON.getJSONArray("message");
            onWaitFragmentInteractionHide();
            if (success) {
                onWaitFragmentInteractionHide();
                for (int j = 0; j < data.length(); j++) {
                    mEmails.add(data.getString(j));
                }
                //When done parsing begin creating list of connections
                for (int i = 0; i < mEmails.size(); i++) {
                        Connection conn = new Connection.Builder(mEmails.get(i)).build();
                        mConnections.add(conn);
                }


                    //Bundle list of connections as arguments and load connection fragment
                    Connection[] connectionsAsArray = new Connection[mConnections.size()];
                    connectionsAsArray = mConnections.toArray(connectionsAsArray);
                    //Bundle connections and send as arguments
                    Bundle args = new Bundle();
                    args.putSerializable(InvitationsFragment.ARG_INVITATION_LIST, connectionsAsArray);
                Fragment frag = new InvReqFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_messaging_container, frag );
                transaction.commit();

                    frag = new InvitationsFragment();
                    frag.setArguments(args);
                    transaction = getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_messaging_inv_container, frag );
                    transaction.commit();

                Log.e("super!!!!!", "yup");


            }
            onWaitFragmentInteractionHide();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("SUPER!!", e.getMessage());
            //notify user
            onWaitFragmentInteractionHide();
        }

    }


    @Override
    public void onInvitationAcceptFragmentInteraction(Connection item) {
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_acceptinvitation))
                .build();
        String msg = getIntent().getExtras().getString("email");
        Credentials creds = new Credentials.Builder(msg).build();
        getSupportActionBar().setTitle("Invitations");
        new SendPostAsyncTask.Builder(uri.toString(),creds.asJSONObject())
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleInvitationAcceptOnPostExecute)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
    }

    private void handleInvitationAcceptOnPostExecute(String result) {
        //parse JSON
        try {
            mEmails = new ArrayList<>();
            mConnections = new ArrayList<>();
            JSONObject resultJSON = new JSONObject(result);
            boolean success = resultJSON.getBoolean("success");
            onWaitFragmentInteractionHide();
            if (success) {
                onWaitFragmentInteractionHide();

                Uri uri2 = new Uri.Builder()
                        .scheme("https")
                        .appendPath(getString(R.string.ep_base_url))
                        .appendPath(getString(R.string.ep_getinvitations))
                        .build();
                String msg2 = getIntent().getExtras().getString("email");
                Credentials creds2 = new Credentials.Builder(msg2).build();
                getSupportActionBar().setTitle("Connections");
                new SendPostAsyncTask.Builder(uri2.toString(),creds2.asJSONObject())
                        .onPreExecute(this::onWaitFragmentInteractionShow)
                        .onPostExecute(this::handleInvitationGetOnPostExecute)
                        .onCancelled(this::handleErrorsInTask)
                        .build().execute();
                Log.e("super duper!!!!!", "yup");


            }
            onWaitFragmentInteractionHide();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("SUPER!!", e.getMessage());
            //notify user
            onWaitFragmentInteractionHide();
        }


    }

    @Override
    public void onInvitationDeclineFragmentInteraction(Connection item) {


        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_declineinvitation))
                .build();
        String msg = getIntent().getExtras().getString("email");
        Credentials creds = new Credentials.Builder(msg).build();
        getSupportActionBar().setTitle("Invitations");
        new SendPostAsyncTask.Builder(uri.toString(),creds.asJSONObject())
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleInvitationDeclineOnPostExecute)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
    }

    private void handleInvitationDeclineOnPostExecute(String result) {

        //parse JSON
        try {
            mEmails = new ArrayList<>();
            mConnections = new ArrayList<>();
            JSONObject resultJSON = new JSONObject(result);
            boolean success = resultJSON.getBoolean("success");
            onWaitFragmentInteractionHide();
            if (success) {
                onWaitFragmentInteractionHide();

                Uri uri2 = new Uri.Builder()
                        .scheme("https")
                        .appendPath(getString(R.string.ep_base_url))
                        .appendPath(getString(R.string.ep_getinvitations))
                        .build();
                String msg2 = getIntent().getExtras().getString("email");
                Credentials creds2 = new Credentials.Builder(msg2).build();
                getSupportActionBar().setTitle("Connections");
                new SendPostAsyncTask.Builder(uri2.toString(),creds2.asJSONObject())
                        .onPreExecute(this::onWaitFragmentInteractionShow)
                        .onPostExecute(this::handleInvitationGetOnPostExecute)
                        .onCancelled(this::handleErrorsInTask)
                        .build().execute();
                Log.e("super duper!!!!!", "yup");


            }
            onWaitFragmentInteractionHide();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("SUPER!!", e.getMessage());
            //notify user
            onWaitFragmentInteractionHide();
        }


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    // Deleting the Pushy device token must be done asynchronously. Good thing
    // we have something that allows us to do that.
    class DeleteTokenAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onWaitFragmentInteractionShow();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //since we are already doing stuff in the background, go ahead
            //and remove the credentials from shared prefs here.
            SharedPreferences prefs =
                    getSharedPreferences(
                            getString(R.string.keys_shared_prefs),
                            Context.MODE_PRIVATE);
            prefs.edit().remove(getString(R.string.keys_prefs_password)).apply();
            prefs.edit().remove(getString(R.string.keys_prefs_email)).apply();
            //unregister the device from the Pushy servers
            Pushy.unregister(MessagingHomeActivity.this);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //close the app
            finishAndRemoveTask();
            //or close this activity and bring back the Login
            // Intent i = new Intent(this, MainActivity.class);
            // startActivity(i);
            // //Ends this Activity and removes it from the Activity back stack.
            // finish();
        }
    }
    /**
     * A BroadcastReceiver that listens for messages sent from PushReceiver
     */
    private class PushMessageReceiver extends BroadcastReceiver {
        private static final String CHANNEL_ID = "1";
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("in push message receive---+++++->MainActivity--->>>>>><<<."+intent.toString());
            if(intent.hasExtra("SENDER") && intent.hasExtra("MESSAGE")) {
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
}
