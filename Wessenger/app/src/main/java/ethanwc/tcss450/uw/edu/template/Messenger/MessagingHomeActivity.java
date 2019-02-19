package ethanwc.tcss450.uw.edu.template.Messenger;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ethanwc.tcss450.uw.edu.template.Connections.SendPostAsyncTask;
import ethanwc.tcss450.uw.edu.template.Main.MainActivity;
import ethanwc.tcss450.uw.edu.template.Main.WaitFragment;
import ethanwc.tcss450.uw.edu.template.Messenger.AddContactFragment.OnNewContactFragmentButtonAction;
import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.dummy.DummyContent;
import ethanwc.tcss450.uw.edu.template.model.Connection;
import ethanwc.tcss450.uw.edu.template.model.Credentials;
import ethanwc.tcss450.uw.edu.template.weather.ChangeLocationsFragment;
import ethanwc.tcss450.uw.edu.template.weather.SavedLocationFragment;
import ethanwc.tcss450.uw.edu.template.weather.WeatherHome;
import me.pushy.sdk.Pushy;

/**
 * Acivity class which manages functionality beyond login/registration.
 */
public class MessagingHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NewMessageFragment.OnSendBtnNewMessage,
        ConversationFragment.OnListFragmentInteractionListener, ConnectionsFragment.OnConnectionListFragmentInteractionListener,
        InvitationsFragment.OnListFragmentInteractionListener, RequestsFragment.OnListFragmentInteractionListener,
        SavedLocationFragment.OnListFragmentInteractionListener, WeatherHome.OnFragmentInteractionListener,
        ChangePasswordFragment.OnChangePasswordFragmentInteractionListener, OnNewContactFragmentButtonAction {


    private Bundle mArgs;
    private FloatingActionButton mFab;
    private ArrayList<String> mEmailList;
    private static final String[] COUNTRIES = new String[] { "Belgium",
            "France", "France_", "Italy", "Germany", "Spain" };

    /**
     * OnCreate used to instantiate the starting state of the application.
     * @param savedInstanceState Bundle of instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging_home);
        Toolbar toolbar = findViewById(R.id.toolbar_messenging_toolbar);
        setSupportActionBar(toolbar);

        //Hide the FAB upon main activity loading.
        mFab = findViewById(R.id.fab_messaging_fab);
        mFab.setEnabled(false);
        mFab.hide();

        if(getIntent().getExtras() != null){
            Bundle extras = getIntent().getExtras();

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
            private void loadFragment(Fragment frag){
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_messaging_container, frag )
                        .addToBackStack(null);
                transaction.commit();
            }
        });

        //Setup the navigation
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

    /**
     * OnBackPressed used to handle minimizing of the navigation drawer and closing the FAB on inappropriate windows.
     */
    @Override
    public void onBackPressed() {
        View connectionViewFrag = findViewById(R.id.fragment_messaging_connectionView);
        View addcontactViewFrag = findViewById(R.id.fragment_messenger_addcontact);
        @SuppressWarnings("RedundantCast") DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_messaging_container);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);


        } else if(connectionViewFrag != null || addcontactViewFrag != null) {
            //Show the FAB on correct windows when back is pressed.
            mFab.show();
            mFab.setEnabled(true);
            super.onBackPressed();
        } else {
            //Hide the FAB on correct windows when back is pressed
            mFab.hide();
            mFab.setEnabled(false);
            super.onBackPressed();
        }
    }

    /**
     * OnCreateOptionsMenu used to help create the options menu.
     * @param menu Encompassing menu.
     * @return Boolean used to represent if the menu has been created.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the search menu action bar.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.messaging_home, menu);

        // Get the search menu.
        MenuItem searchMenu = menu.findItem(R.id.app_bar_menu_search);

        // Get SearchView object.
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenu);

        // Get SearchView autocomplete object.
        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setBackgroundColor(Color.BLUE);
        searchAutoComplete.setTextColor(Color.GREEN);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.holo_blue_light);
        String dataArr[]= new String[mEmailList.size()];
        for(int i=0; i<mEmailList.size(); i++){
            dataArr[i]= mEmailList.get(i);
        }

        ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, dataArr);
        searchAutoComplete.setAdapter(newsAdapter);

        // Listen to search view item on click event.
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                String queryString=(String)adapterView.getItemAtPosition(itemIndex);
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

    /**
     * Method used to handle when menu items are selected.
     * @param item MenuItem used to represent the item that has been selected.
     * @return Boolean used to represent whether the item has been selected.
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //Chat has been chosen
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
        //Weather home has been chosen
        if (id == R.id.nav_weather_home) {

            WeatherHome weatherHome = new WeatherHome();
            getSupportActionBar().setTitle("Weather Home");
            loadFragment(weatherHome);
        //Change locations has been chosen
        } else if (id == R.id.nav_Change_Locations) {
            ChangeLocationsFragment changeLocationsFragment = new ChangeLocationsFragment();
            getSupportActionBar().setTitle("Change Location");
            loadFragment(changeLocationsFragment);
        //Saved locations has been chosen
        } else if (id == R.id.nav_View_Saved_Location) {
            SavedLocationFragment locationFragment = new SavedLocationFragment();
            getSupportActionBar().setTitle("Saved Location");
            loadFragment(locationFragment);
        //Messenger home has been chosen
        } else if (id == R.id.nav_chat_home) {
            ConversationFragment conversationFragment = new ConversationFragment();
            getSupportActionBar().setTitle("Messaging Home");


            loadFragment(conversationFragment);
        //Connections has been chosen
        } else if (id == R.id.nav_chat_view_connections) {

            //Build ASNC task to grab connections from web service.
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_getContacts))
                  .build();
            String msg = getIntent().getExtras().getString("email");
            Credentials creds = new Credentials.Builder(msg).build();
            getSupportActionBar().setTitle("Connections");
            new SendPostAsyncTask.Builder(uri.toString(),creds.asJSONObject())
                    .onPreExecute(this::onWaitFragmentInteractionShow)
                    .onPostExecute(this::handleConnectionGetOnPostExecute)
                    .onCancelled(this::handleErrorsInTask)
                    .build().execute();
            //Show FAB
            mFab.setEnabled(true);
            mFab.show();
        //Requests/Invitations has been chosen
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
     * @param frag
     */
    private void loadFragment(Fragment frag){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_messaging_container, frag )
                .addToBackStack(null);
        transaction.commit();
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

        new SendPostAsyncTask.Builder(uri.toString(),json)
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleConnectionDeleteOnPostExecute)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();

        //Build ASYNC task to get the new contacts list.
        uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_getContacts))
                .build();
        msg = getIntent().getExtras().getString("email");
        Credentials creds = new Credentials.Builder(msg).build();
        getSupportActionBar().setTitle("Connections");
        new SendPostAsyncTask.Builder(uri.toString(),creds.asJSONObject())
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleConnectionGetOnPostExecute)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
        mFab.setEnabled(true);
        mFab.show();

    }

    /**
     *
     * @param item
     */
    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    /**
     * Helper method used to handle the tasks after the async task has been completed for receiving contact list.
     * @param result String which represents the JSON result.
     */
    private void handleConnectionGetOnPostExecute(final String result) {
        //parse JSON
        try {
            JSONObject resultJSON = new JSONObject(result);
            boolean success = resultJSON.getBoolean("success");
            JSONArray data = resultJSON.getJSONArray("message");

            if (success) {

                //Create list of connections
                List<Connection> connections = new ArrayList<>();
                for(int i = 0; i < data.length(); i++) {

                    String email = data.getString(i);
                     connections.add(new Connection.Builder(email).build());
                }

                Connection[] connectionsAsArray = new Connection[connections.size()];
                connectionsAsArray = connections.toArray(connectionsAsArray);
                //Bundle connections and send as arguments
                Bundle args = new Bundle();
                args.putSerializable(ConnectionsFragment.ARG_CONNECTION_LIST, connectionsAsArray);
                Fragment frag = new ConnectionsFragment();
                frag.setArguments(args);

                onWaitFragmentInteractionHide();
                loadFragment(frag);


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
     * @param result String which represents the JSON result.
     */
    private void handleConnectionDeleteOnPostExecute(final String result) {
        //parse JSON
        try {
            JSONObject resultJSON = new JSONObject(result);
            boolean success = resultJSON.getBoolean("success");

            if (success) {
                //Hide wait fragment to go on to next ASYNC call
                onWaitFragmentInteractionHide();
                //Not successful return from webservice
            } else {
                //Delete unesuccessful
                TextView delete = findViewById(R.id.textview_connections_details_deletebutton);
                delete.setText("Unexpected error.");
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
     * Method used to handle the tasks after the ASYNC task returns for adding a contact.
     * @param result String which represents the JSON result.
     */
    private void handleConnectionAddOnPostExecute(final String result) {
        //parse JSON
        try {
            JSONObject resultJSON = new JSONObject(result);
            boolean success = resultJSON.getBoolean("success");

            if (success) {
                //Hide wait fragment for next ASYNC task
                onWaitFragmentInteractionHide();
                //Not successful return from webservice
            } else {
                //Add was unsuccessful, let user know the email is unavailable for adding.
                EditText email = findViewById(R.id.edittext_newcontact_email);
                email.setText("The email entered is not in our system. Try another email.");
                onWaitFragmentInteractionHide();
            }
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

    /**
     * Method which listens for adding a contact button.
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
        new SendPostAsyncTask.Builder(uri.toString(),json)
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleConnectionAddOnPostExecute)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();

        //Build ASYNC task for getting new contact list
        uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_getContacts))
                .build();
        msg = getIntent().getExtras().getString("email");

        Credentials creds = new Credentials.Builder(msg).build();

        getSupportActionBar().setTitle("Connections");
        new SendPostAsyncTask.Builder(uri.toString(),creds.asJSONObject())
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleConnectionGetOnPostExecute)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
        mFab.setEnabled(true);
        mFab.show();

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
