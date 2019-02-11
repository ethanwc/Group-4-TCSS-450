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
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ethanwc.tcss450.uw.edu.template.Main.WaitFragment;
import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.dummy.DummyContent;
import ethanwc.tcss450.uw.edu.template.model.Credentials;
import ethanwc.tcss450.uw.edu.template.weather.ChangeLocationsFragment;
import ethanwc.tcss450.uw.edu.template.weather.SavedLocationFragment;
import ethanwc.tcss450.uw.edu.template.weather.WeatherHome;
import me.pushy.sdk.Pushy;

public class MessagingHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NewMessageFragment.OnSendBtnNewMessage,
        ConversationFragment.OnListFragmentInteractionListener, ConnectionsFragment.OnListFragmentInteractionListener,
        InvitationsFragment.OnListFragmentInteractionListener, RequestsFragment.OnListFragmentInteractionListener,
        SavedLocationFragment.OnListFragmentInteractionListener, WeatherHome.OnFragmentInteractionListener, WaitFragment.OnFragmentInteractionListener {


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
        if (id == R.id.action_logout) {
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
            Credentials c = (Credentials) getIntent().getExtras().getSerializable(getString(R.string.email_registerToLogin));
            String email = c.getEmail();

            Bundle args = new Bundle();
            args.putString("jwt_token", jwt);
            args.putString("email_token_123", email);
            Fragment chatFrag = new ChatFragment();
            chatFrag.setArguments(args);


            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.activity_messaging_container, chatFrag)
                    .addToBackStack(null);
            // Commit the transaction
            transaction.commit();
        }
        if (id == R.id.nav_weather_home) {

            WeatherHome weatherHome = new WeatherHome();

            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_messaging_container, weatherHome)
                    .addToBackStack(null);
            getSupportActionBar().setTitle("Weather Home");
            transaction.commit();
//            Intent intent = new Intent(MessagingHomeActivity.this, WeatherHomeActivity.class);
//            startActivity(intent);
        } else if (id == R.id.nav_Change_Locations) {
//            loadFragment(new ChangeLocationsFragment());

            ChangeLocationsFragment changeLocationsFragment = new ChangeLocationsFragment();

            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_messaging_container, changeLocationsFragment)
                    .addToBackStack(null);
            getSupportActionBar().setTitle("Change Location");
            transaction.commit();

        } else if (id == R.id.nav_View_Saved_Location) {
//            loadFragment(new SavedLocationFragment());
            SavedLocationFragment locationFragment = new SavedLocationFragment();

            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_messaging_container, locationFragment)
                    .addToBackStack(null);
            getSupportActionBar().setTitle("Saved Location");
            transaction.commit();
        } else if (id == R.id.nav_chat_home) {
//            Intent intent = new Intent(MessagingHomeActivity.this, MessagingHomeActivity.class);
//            startActivity(intent);

            ConversationFragment conversationFragment = new ConversationFragment();
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_messaging_container, conversationFragment)
                    .addToBackStack(null);
            getSupportActionBar().setTitle("Messaging Home");
            transaction.commit();
        } else if (id == R.id.nav_chat_view_connections) {
            ConnectionsFragment connectionsFragment = new ConnectionsFragment();
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_messaging_container, connectionsFragment)
                    .addToBackStack(null);
            getSupportActionBar().setTitle("Connections");
            transaction.commit();

        } else if (id == R.id.nav_Request_Invitations) {
            InvitationsFragment invitationsFragment = new InvitationsFragment();
            //RequestsFragment requestsFragment = new RequestsFragment();
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()

                    .replace(R.id.fragment_messaging_container, invitationsFragment)
                    //.replace(R.id.secondFragmentContainer, requestsFragment)
                    .addToBackStack(null);
            getSupportActionBar().setTitle("Requests/Invitations");
            transaction.commit();
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
        finishAndRemoveTask();
        //or close this activity and bring back the Login
// Intent i = new Intent(this, MainActivity.class);
// startActivity(i);
// //Ends this Activity and removes it from the Activity back stack.
// finish();
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
        MessageViewFragment messageView = new MessageViewFragment();
        loadFragment(messageView);
    }

    private void loadFragment(Fragment frag){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_messaging_container, frag )
                .addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
}
