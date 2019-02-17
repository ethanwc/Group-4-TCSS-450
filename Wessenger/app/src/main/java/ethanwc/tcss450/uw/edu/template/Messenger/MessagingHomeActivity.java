package ethanwc.tcss450.uw.edu.template.Messenger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ethanwc.tcss450.uw.edu.template.Connections.GetAsyncTask;
import ethanwc.tcss450.uw.edu.template.Connections.SendPostAsyncTask;
import ethanwc.tcss450.uw.edu.template.Main.MainActivity;
import ethanwc.tcss450.uw.edu.template.Main.WaitFragment;
import ethanwc.tcss450.uw.edu.template.Main.WaitFragment.OnFragmentInteractionListener;
import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.dummy.DummyContent;
import ethanwc.tcss450.uw.edu.template.model.Connection;
import ethanwc.tcss450.uw.edu.template.model.Credentials;
import ethanwc.tcss450.uw.edu.template.weather.ChangeLocationsFragment;
import ethanwc.tcss450.uw.edu.template.weather.SavedLocationFragment;
import ethanwc.tcss450.uw.edu.template.weather.WeatherHome;
import me.pushy.sdk.Pushy;

public class MessagingHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NewMessageFragment.OnSendBtnNewMessage,
        ConversationFragment.OnListFragmentInteractionListener, ConnectionsFragment.OnConnectionListFragmentInteractionListener,
        InvitationsFragment.OnListFragmentInteractionListener, RequestsFragment.OnListFragmentInteractionListener,
        SavedLocationFragment.OnListFragmentInteractionListener, WeatherHome.OnFragmentInteractionListener,
        ChangePasswordFragment.OnChangePasswordFragmentInteractionListener {


    private Bundle mArgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging_home);
        Toolbar toolbar = findViewById(R.id.toolbar_messenging_toolbar);
        setSupportActionBar(toolbar);

        //TODO: Implement this correctly

//        FloatingActionButton fab = findViewById(R.id.fab_messenging_fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                loadFragment(new NewMessageFragment());
//            }
//            private void loadFragment(Fragment frag){
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.activity_messaging_container, frag )
//                        .addToBackStack(null);
//                transaction.commit();
//            }
//        });

//        FloatingActionButton fab = findViewById(R.id.fab_messenging_fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                loadFragment(new NewMessageFragment());
//            }
//            private void loadFragment(Fragment frag){
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_messaging_container, frag )
//                        .addToBackStack(null);
//                getSupportActionBar().setTitle("New Message");
//                transaction.commit();
//            }
//        });

        DrawerLayout drawer = findViewById(R.id.activity_messaging_container);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.navview_messanging_nav);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment;
        if (getIntent().getBooleanExtra(getString(R.string.keys_intent_notification_msg), false)) {
            fragment = new ChatFragment();
        } else {
            fragment = new ConversationFragment();
        }



        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_messaging_container, fragment)
                .commit();

    }

    @Override
    public void onBackPressed() {
        @SuppressWarnings("RedundantCast") DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_messaging_container);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.messaging_home, menu);
        return true;
    }

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_global_chat) {


            String jwt = getIntent().getExtras().getString(getString(R.string.keys_intent_jwt));
            String email = getIntent().getExtras().getString(("email"));
            Bundle args = new Bundle();
            args.putString("jwt_token", jwt);
            args.putString("email_token_123", email);
            Fragment chatFrag = new ChatFragment();
            chatFrag.setArguments(args);


            loadFragment(chatFrag);
        }
        if (id == R.id.nav_weather_home) {

            WeatherHome weatherHome = new WeatherHome();
            getSupportActionBar().setTitle("Weather Home");
            loadFragment(weatherHome);
        } else if (id == R.id.nav_Change_Locations) {
            ChangeLocationsFragment changeLocationsFragment = new ChangeLocationsFragment();
            getSupportActionBar().setTitle("Change Location");
            loadFragment(changeLocationsFragment);

        } else if (id == R.id.nav_View_Saved_Location) {
            SavedLocationFragment locationFragment = new SavedLocationFragment();
            getSupportActionBar().setTitle("Saved Location");
            loadFragment(locationFragment);
        } else if (id == R.id.nav_chat_home) {
            ConversationFragment conversationFragment = new ConversationFragment();
            getSupportActionBar().setTitle("Messaging Home");
            loadFragment(conversationFragment);
        } else if (id == R.id.nav_chat_view_connections) {


            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_getContacts))
                  .build();
            String msg = getIntent().getExtras().getString("email");
            Credentials creds = new Credentials.Builder(msg).build();

            new SendPostAsyncTask.Builder(uri.toString(),creds.asJSONObject())
                    .onPreExecute(this::onWaitFragmentInteractionShow)
                    .onPostExecute(this::handleConnectionGetOnPostExecute)
                    .onCancelled(this::handleErrorsInTask)
                    .build().execute();
//            new GetAsyncTask.Builder(uri.toString())
//                    .onPreExecute(this::onWaitFragmentInteractionShow)
//
//                    .onPostExecute(this::handleConnectionGetOnPostExecute)
//                    .build().execute();

        } else if (id == R.id.nav_Request_Invitations) {
            InvitationsFragment invitationsFragment = new InvitationsFragment();
            getSupportActionBar().setTitle("Requests/Invitations");
            loadFragment(invitationsFragment);
        }

        DrawerLayout drawer = findViewById(R.id.activity_messaging_container);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

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

    public void onWaitFragmentInteractionHide() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentByTag("WAIT"))
                .commit();
    }

    //
    @Override
    public void onSendFragmentInteraction() {
    }

    /**
     * Load the desire fragment
     * @param frag
     */
    private void loadFragment(Fragment frag){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_messaging_container, frag )
                .addToBackStack(null);
        transaction.commit();
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * Method which listens for a connection being selected in the recycler view.
     * @param theItem Connection which will represent a connection.
     */
    @Override
    public void onConnectionListFragmentInteraction(Connection theItem) {

        ConnectionViewFragment connectionViewFrag;
        connectionViewFrag = new ConnectionViewFragment();

        Bundle args = new Bundle();

        args.putSerializable("username", theItem.getUsername());
        args.putSerializable("first", theItem.getFirst());
        args.putSerializable("last", theItem.getLast());
        args.putSerializable("email", theItem.getEmail());



        connectionViewFrag.setArguments(args);
        loadFragment(connectionViewFrag);

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    private void handleConnectionGetOnPostExecute(final String result) {
        //parse JSON
        try {
            JSONObject resultJSON = new JSONObject(result);
            boolean success = resultJSON.getBoolean("success");
            JSONArray data = resultJSON.getJSONArray("message");

            if (success) {

                List<Connection> connections = new ArrayList<>();


                for(int i = 0; i < data.length(); i++) {

                    String email = data.getString(i);
                    Log.e("SUPER!!!", email);

                    connections.add(new Connection.Builder(email).build());
                    //JSONObject jsonConnection = data.getJSONObject(i);

//                    connections.add(new Connection.Builder(
//                            jsonConnection.getString(getString(R.string.keys_json_connections_first)),
//                            jsonConnection.getString(getString(R.string.keys_json_connections_last)),
//                            jsonConnection.getString(getString(R.string.keys_json_connections_username)),
//                            jsonConnection.getString(getString(R.string.keys_json_connections_email))).build());

                }

                Connection[] connectionsAsArray = new Connection[connections.size()];
                connectionsAsArray = connections.toArray(connectionsAsArray);

                Bundle args = new Bundle();
                args.putSerializable(ConnectionsFragment.ARG_CONNECTION_LIST, connectionsAsArray);
                Fragment frag = new ConnectionsFragment();
                frag.setArguments(args);

                onWaitFragmentInteractionHide();
                loadFragment(frag);

            //Not successful return from webservice
            } else {
                Log.e("SUPER!!", "NOT SUCCESS");

                onWaitFragmentInteractionHide();
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("SUPER!!", e.getMessage());
            //notify user
            onWaitFragmentInteractionHide();
        }

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

    @Override
    public void onChangePasswordClicked() {
        getSupportActionBar().setTitle("Messaging");
        loadFragment(new ConversationFragment());
    }
}
