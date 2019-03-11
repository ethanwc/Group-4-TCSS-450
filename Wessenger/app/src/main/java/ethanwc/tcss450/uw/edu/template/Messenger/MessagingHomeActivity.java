package ethanwc.tcss450.uw.edu.template.Messenger;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.text.emoji.EmojiCompat;
import android.support.text.emoji.FontRequestEmojiCompatConfig;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.provider.FontRequest;
import android.support.v4.view.GravityCompat;
import android.support.v4.provider.FontRequest;
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
import android.widget.ImageButton;
import android.widget.TextView;

import com.cloudinary.android.MediaManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ethanwc.tcss450.uw.edu.template.Connections.GetAsyncTask;
import ethanwc.tcss450.uw.edu.template.Connections.SendPostAsyncTask;
import ethanwc.tcss450.uw.edu.template.Main.MainActivity;
import ethanwc.tcss450.uw.edu.template.Main.WaitFragment;
import ethanwc.tcss450.uw.edu.template.Messenger.AddContactFragment.OnNewContactFragmentButtonAction;
import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.Weather.ChangeLocationsFragment;
import ethanwc.tcss450.uw.edu.template.Weather.CurrentWeather;
import ethanwc.tcss450.uw.edu.template.Weather.DailyWeatherFragment;
import ethanwc.tcss450.uw.edu.template.Weather.HourlyWeatherFragment;
import ethanwc.tcss450.uw.edu.template.Weather.SavedLocationFragment;
import ethanwc.tcss450.uw.edu.template.Weather.SavedLocationViewFragment;
import ethanwc.tcss450.uw.edu.template.Weather.WeatherHome;
import ethanwc.tcss450.uw.edu.template.model.Connection;
import ethanwc.tcss450.uw.edu.template.model.Credentials;
import ethanwc.tcss450.uw.edu.template.model.DailyWeather;
import ethanwc.tcss450.uw.edu.template.model.HourlyWeather;
import ethanwc.tcss450.uw.edu.template.model.Message;
import ethanwc.tcss450.uw.edu.template.model.location;
import ethanwc.tcss450.uw.edu.template.temp.ChatFragment2;
import me.pushy.sdk.Pushy;
import me.pushy.sdk.lib.jackson.core.io.JsonEOFException;

/**
 * Acivity class which manages functionality beyond login/registration.
 */
public class MessagingHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NewMessageFragment.OnSendBtnNewMessage,
        ConversationFragment.OnMessageListFragmentInteractionListener, ConnectionsFragment.OnConnectionListFragmentInteractionListener,
        InvitationsFragment.OnInvitationListFragmentInteractionListener,
        ChangePasswordFragment.OnChangePasswordFragmentInteractionListener,
        SavedLocationFragment.OnLocationListFragmentInteractionListener,
        OnNewContactFragmentButtonAction,
        WeatherHome.OnFragmentInteractionListener,
        ChatFragment2.OnChatFragmentButtonAction, AddToChatFragment.OnAddToChatFragmentAction,
        RemoveFromChatFragment.OnRemoveFromChatFragmentAction, AddChatFragment.OnAddChatFragmentAction,
        HomeFragment.OnHomeFragmentInteractionListener,
        CurrentWeather.OnCurrentWeatherUpdateListener,
        DailyWeatherFragment.OnListFragmentInteractionListener,
        HourlyWeatherFragment.OnListFragmentInteractionListener,
        ChangeLocationsFragment.onChangeLocationFragmentInteractionListener {


    private Bundle mArgs;
    private FloatingActionButton mFab;
    private ArrayList<String> mEmailList;
    private ArrayList<String> mEmails;
    private ArrayList<String> mFirsts;
    private ArrayList<String> mLasts;
    private ArrayList<String> mUNames;
    private ArrayList<String> mChats;
    private Map<String, String> mPeople;
    private Map<String, String> mChatNames;
    Multimap<String, String> mChatMembers;
    private ArrayList<Connection> mConnections;
    private int mCounter = 0;
    DrawerLayout mdrawer;
    private int mZip = 98404;
    private String mChatId = "";
    private ArrayList<location> mLocation;
    private List<DailyWeather> mDailyWeather;
    private BroadcastReceiver _refreshReceiver;
    private boolean mWeather = true;
    private boolean fromMaps = false;
    private PushMessageReceiver mPushMessageReciever;
    private static final String TAG = "MessagingHomeActivity";
    private boolean mIsMedia = false;
    private Connection[] mConnectionsAsArray;
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private static final int MY_PERMISSIONS_LOCATIONS = 8414;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
//    private MenuItem mMenuItem;

    private static final String[] COUNTRIES = new String[]{"Belgium",
            "France", "France_", "Italy", "Germany", "Spain"};



//public void setColortitle(){
//    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0000ff")));
//}
    /**
     * OnCreate used to instantiate the starting state of the application.
     *
     * @param savedInstanceState Bundle of instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging_home);
        //init cloudinary stuffs
        _refreshReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mZip = intent.getExtras().getInt("zip");
                fromMaps = true;

            }
        };
        //for location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION
                            , Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_LOCATIONS);
            System.out.println("----------not granted----");
        } else {
            System.out.println("---------- granted----");
//The user has already allowed the use of Locations. Get the current location.
 requestLocation();
        }
//-------
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
// Update UI with location data
// ...
                    setLocation(location);
                    Log.d("LOCATION UPDATE!", mCurrentLocation.toString());
                } };
        };
        createLocationRequest();

        //
        if (!mIsMedia) {
            MediaManager.init(this);
            mIsMedia = true;
        }
        setupEmojis();

        setContentView(R.layout.activity_messaging_home);
        Toolbar toolbar = findViewById(R.id.toolbar_messenging_toolbar);
        setSupportActionBar(toolbar);

        //loadChats();
        //Hide the FAB upon main activity loading.
        mFab = findViewById(R.id.fab_messaging_fab);
        mFab.setEnabled(true);
        mFab.show();

        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            //System.out.println("+=======from pushy");
            mEmailList = new ArrayList<String>();
            mEmailList = getIntent().getStringArrayListExtra("a");
        }

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loadFragment(new AddChatFragment());
                        mFab.hide();
                        mFab.setEnabled(false);
                    }
                });
            }
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
            fragment = new ChatFragment2();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_messaging_container, fragment)
                    .commit();
        } else if (getIntent().getBooleanExtra(getString(R.string.keys_intent_notification_invitation), false)) {
            InvitationsFragment invitation = new InvitationsFragment();
//            fragment = new InvitationsFragment();
//            System.out.println("invitation intent------>"+getIntent().getExtras().getString(("email")));
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_messaging_container, invitation)
                    .commit();
        } else {
            loadChats();

        }

    }

    private void loadWeather() {
        WeatherHome weatherHome = new WeatherHome();
        Bundle args = new Bundle();
        args.putSerializable("zip", mZip);
        weatherHome.setArguments(args);
        getSupportActionBar().setTitle("Weather Home");
        mFab.hide();
        mFab.setEnabled(false);

        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath("api.openweathermap.org/data/2.5/forecast?zip=" + mZip + "&cnt=10&appid=b0ce6ca6ee362ce9ea5bbe361fdcbf92")
                .build();

        new GetAsyncTask.Builder("https://api.openweathermap.org/data/2.5/forecast?zip=" + mZip + "&cnt=10&appid=b0ce6ca6ee362ce9ea5bbe361fdcbf92")//uri.toString()
//                    .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleWeatherPostExecute)
                .onCancelled(this::handleErrorsInTask)
                .build()
                .execute();
        onWaitFragmentInteractionHide();

        //mFab.setImageResource(R.drawable.ic_saved_location);
        mFab.show();
//            mFab.setImageResource(android.R.drawable.ic_menu_save);

        mFab.setEnabled(true);
    }
    private void setLocation(final Location location) {
        mCurrentLocation = location;

    }
    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter("zipCodeSent");
        this.registerReceiver(_refreshReceiver, filter);
        if (mZip != 0 && fromMaps) {
            fromMaps = false;
            loadWeather();

        }
        startLocationUpdates(); }
    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates(); }
    /**
     * Requests location updates from the FusedLocationApi. */
    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback,
                    null /* Looper */); }
    }


    /**
     * Removes location updates from the FusedLocationApi. */
    protected void stopLocationUpdates() {
// It is a good practice to remove location requests when the activity is in a paused or
// stopped state. Doing so helps battery performance and is especially
// recommended in applications that request frequent location updates.
 mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_LOCATIONS: {
// If request is cancelled, the result arrays are empty.
 if (grantResults.length > 0
&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
// permission was granted, yay! Do the // locations-related task you need to do.
 requestLocation();
            } else {
// permission denied, boo! Disable the
// functionality that depends on this permission.
 Log.d("PERMISSION DENIED", "Nothing to see or do here.");
 System.out.println("-----Permission Denied");
//Shut down the app. In production release, you would let the user
// know why the app is shutting down...maybe ask for permission again?
 finishAndRemoveTask();
            } return;
        }
// other 'case' lines to check for other
// permissions this app might request
 }
    }

    private void requestLocation() {
        System.out.println("request location----------");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            System.out.println("request location denied----------");
            Log.d("REQUEST LOCATION", "User did NOT allow permission to request location!");
        } else {
            System.out.println("request location success----------");
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            System.out.println(" onsuccess----------");
// Got last known location. In some rare situations this can be null.
 if (location != null) {
                            Log.d("LOCATION", location.toString());
                        } }
        }); }
}

    /**
     * Create and configure a Location Request used when retrieving location updates */
    protected void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
// Sets the desired interval for active location updates. This interval is
// inexact. You may not receive updates at all if no location sources are available, or
// you may receive them slower than requested. You may also receive updates faster than
// requested if other applications are requesting location at a faster interval.
 mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
// Sets the fastest rate for active location updates. This interval is exact, and your
// application will never receive updates faster than this value.
 mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }



    /**
     * Initializes the download of emojis.
     */
    public void setupEmojis() {
        final EmojiCompat.Config config;

            // Use a downloadable font for EmojiCompat
            final FontRequest fontRequest = new FontRequest(
                    "com.google.android.gms.fonts",
                    "com.google.android.gms",
                    "Noto Color Emoji Compat",
                    R.array.com_google_android_gms_fonts_certs);
            config = new FontRequestEmojiCompatConfig(getApplicationContext(), fontRequest);


        config.setReplaceAll(true)
                .registerInitCallback(new EmojiCompat.InitCallback() {
                    @Override
                    public void onInitialized() {
                        Log.i("EMOJISTUFF", "EmojiCompat initialized");
                    }

                    @Override
                    public void onFailed(@Nullable Throwable throwable) {
                        Log.e("EMOJISTUFF", "EmojiCompat initialization failed", throwable);
                    }
                });

        EmojiCompat.init(config);
    }

    /**
     * OnBackPressed used to handle minimizing of the navigation drawer and closing the FAB on inappropriate windows.
     */
    @Override
    public void onBackPressed() {
        mWeather = true;


        super.onBackPressed();
        View connectionViewFrag = findViewById(R.id.fragment_messaging_connectionView);
        View addcontactViewFrag = findViewById(R.id.fragment_messenger_addcontact);
        View conversationViewFrag = findViewById(R.id.fragment_messagelist_conversation);
        View chatFrag = findViewById(R.id.fragment_messaging_message);
        View addChatFrag = findViewById(R.id.fragment_messenger_addchat);
        View changelocation = findViewById( R.id.fragment_weather_changelocation );
        View currentWeather = findViewById(R.id.fragment_weather_current_main);
        @SuppressWarnings("RedundantCast") DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_messaging_container);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);


        } else if (connectionViewFrag != null || addcontactViewFrag != null || conversationViewFrag != null
                || addChatFrag != null || chatFrag != null|| changelocation != null) {
            //Show the FAB on correct windows when back is pressed.
            mFab.show();
//            mFab.setImageResource(android.R.drawable.ic_input_add);
            mFab.setEnabled(true);

        } else if (currentWeather != null) {
            mFab.show();
//            mFab.setImageResource(android.R.drawable.ic_menu_save);
            mFab.setEnabled(true);

        } else if (changelocation != null) {
            View removeLocation = findViewById(R.id.button_location_remove);
            removeLocation.setVisibility(View.GONE);
            mFab.show();
//            mFab.setImageResource(android.R.drawable.ic_input_add);
            mFab.setEnabled(true);
            mWeather = false;
        }else {

            //Hide the FAB on correct windows when back is pressed
            mFab.hide();
            mFab.setEnabled(false);

        }
    }

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
        searchAutoComplete.setBackgroundColor(getResources().getColor(R.color.backgroundDark));
        searchAutoComplete.setTextColor(getResources().getColor(R.color.messageText));
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
                Uri uri = new Uri.Builder()
                        .scheme("https")
                        .appendPath(getString(R.string.ep_base_url))
                        .appendPath(getString(R.string.ep_userdetail))
                        .build();
                //handleConnectionGetInfoOnPostExecute
                String msg = getIntent().getExtras().getString("email");
                Credentials creds = new Credentials.Builder(queryString).build();

                new SendPostAsyncTask.Builder(uri.toString(),creds.asJSONObject())
                        .onPreExecute(this::onWaitFragmentInteractionShow)
                        .onPostExecute(this::handleConnectionGetDetailOnPostExecute)
                        .onCancelled(this::handleErrorsInTask)
                        .build().execute();
            }
            public void onWaitFragmentInteractionShow() {
//                System.out.println("======>onwaitshow");
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
            public void handleConnectionGetDetailOnPostExecute(final String result){
                //parse JSON
                try {
//                    System.out.println("======>try");
                    mEmails = new ArrayList<>();
                    mConnections = new ArrayList<>();
                    JSONObject resultJSON = new JSONObject(result);
                    boolean success = resultJSON.getBoolean("success");
                    onWaitFragmentInteractionHide();
                    if (success) {

                        ConnectionViewFragment connectionViewFrag;
                        connectionViewFrag = new ConnectionViewFragment();

                        Bundle args = new Bundle();
                        String email = resultJSON.getString("email");
                        String username = resultJSON.getString("username");
                        String first = resultJSON.getString("firstname");
                        String last = resultJSON.getString("lastname");
                        args.putSerializable("email", email);
                        args.putSerializable("username", username);
                        args.putSerializable("first", first);
                        args.putSerializable("last", last);

                        FloatingActionButton connectionsFab = findViewById(R.id.fab_messaging_fab);
                        connectionsFab.setEnabled(false);
                        connectionsFab.hide();
                        getSupportActionBar().setTitle("User Detail");
                        connectionViewFrag.setArguments(args);
                        loadFragment(connectionViewFrag);
                    }
                    onWaitFragmentInteractionHide();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("SUPER!!", e.getMessage());
                    //notify user
                    onWaitFragmentInteractionHide();
                }


            }
            /**
             * Handle errors that may occur during the AsyncTask.
             *
             * @param result the error message provide from the AsyncTask
             */
            private void handleErrorsInTask(String result) {
                Log.e("ASYNC_TASK_ERROR", result);
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
            Fragment chatFrag = new ChatFragment2();
            chatFrag.setArguments(args);
            getSupportActionBar().setTitle("Global chat");
            mFab.hide();
            mFab.setEnabled(false);

            loadFragment(chatFrag);
        }
        //Weather home has been chosen
        if (id == R.id.nav_weather_home) {

            WeatherHome weatherHome = new WeatherHome();
            Bundle args = new Bundle();
            args.putSerializable("zip", mZip);
            weatherHome.setArguments(args);
            getSupportActionBar().setTitle("Weather Home");
            mFab.hide();
            mFab.setEnabled(false);

            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath("api.openweathermap.org/data/2.5/forecast?zip=" + mZip + "&cnt=10&appid=b0ce6ca6ee362ce9ea5bbe361fdcbf92")
                    .build();

            new GetAsyncTask.Builder("https://api.openweathermap.org/data/2.5/forecast?zip=" + mZip + "&cnt=10&appid=b0ce6ca6ee362ce9ea5bbe361fdcbf92")//uri.toString()
//                    .onPreExecute(this::onWaitFragmentInteractionShow)
                    .onPostExecute(this::handleWeatherPostExecute)
                    .onCancelled(this::handleErrorsInTask)
                    .build()
                    .execute();


            mFab.show();
//            mFab.setImageResource(android.R.drawable.ic_menu_save);
            mFab.setEnabled(true);

            // loadFragment(weatherHome);
            //Change locations has been chosen
        } else if (id == R.id.nav_Change_Locations) {
            mFab.setEnabled(true);
            mFab.show();
//            mFab.setImageResource(android.R.drawable.ic_dialog_map);

            //Set on click listener for FAB

            mWeather = false;

            ChangeLocationsFragment changeLocationsFragment = new ChangeLocationsFragment();
            getSupportActionBar().setTitle("Change Location");



            mLocation = new ArrayList<>();

            //Build ASNC task to grab connections from web service.
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_getLocation))
                    .build();
            //handleConnectionGetInfoOnPostExecute
            String msg = getIntent().getExtras().getString("email");
            Credentials creds = new Credentials.Builder(msg).build();
//            getSupportActionBar().setTitle("Connections");
            new SendPostAsyncTask.Builder(uri.toString(),creds.asJSONObject())
                    .onPreExecute(this::onWaitFragmentInteractionShow)
                    .onPostExecute(this::handleLocationGetPostExecute)
                    .onCancelled(this::handleErrorsInTask)
                    .build().execute();



            loadFragment(changeLocationsFragment);


            //Saved locations has been chosen
        }

        else if (id == R.id.nav_homepage) {
            //Build ASNC task to grab connections from web service.
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_getContacts))
                    .build();
            //handleConnectionGetInfoOnPostExecute
            String msg = getIntent().getExtras().getString("email");
            Credentials creds = new Credentials.Builder(msg).build();
            getSupportActionBar().setTitle("Connections");
            new SendPostAsyncTask.Builder(uri.toString(),creds.asJSONObject())
                    .onPreExecute(this::onWaitFragmentInteractionShow)
                    .onPostExecute(this::handleLoadHomeOnPost)
                    .onCancelled(this::handleErrorsInTask)
                    .build().execute();
        }

        else if (id == R.id.nav_View_Saved_Location) {
            mWeather = true;
            SavedLocationFragment locationFragment = new SavedLocationFragment();
            getSupportActionBar().setTitle("Saved Location");
            mLocation = new ArrayList<>();

            //Build ASNC task to grab connections from web service.
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_getLocation))
                    .build();
            //handleConnectionGetInfoOnPostExecute
            String msg = getIntent().getExtras().getString("email");
            Credentials creds = new Credentials.Builder(msg).build();
//            getSupportActionBar().setTitle("Connections");
            new SendPostAsyncTask.Builder(uri.toString(),creds.asJSONObject())
                    .onPreExecute(this::onWaitFragmentInteractionShow)
                    .onPostExecute(this::handleLocationGetOnPostExecute)
                    .onCancelled(this::handleErrorsInTask)
                    .build().execute();


            mFab.hide();
            mFab.setEnabled(false);

//            loadFragment(locationFragment);
            //Messenger home has been chosen
        }
        else if (id == R.id.nav_chat_home) {
            getSupportActionBar().setTitle("Chat");
            mFab.setEnabled(true);
            mFab.show();
//            mFab.setImageResource(android.R.drawable.ic_input_add);
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadFragment(new AddChatFragment());
                    mFab.hide();
                    mFab.setEnabled(false);
                }

            });
            loadChats();
            //Connections has been chosen
        } else if (id == R.id.nav_chat_view_connections) {
//            mFab.setImageResource(android.R.drawable.ic_input_add);
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
            getSupportActionBar().setTitle("Connections");
            new SendPostAsyncTask.Builder(uri.toString(),creds.asJSONObject())
                    .onPreExecute(this::onWaitFragmentInteractionShow)
                    .onPostExecute(this::handleConnectionGetOnPostExecute)
                    .onCancelled(this::handleErrorsInTask)
                    .build().execute();
            //Show FAB





            mFab.setEnabled(true);
            mFab.show();
//            mFab.setImageResource(android.R.drawable.ic_input_add);
            //Set on click listener for FAB
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    loadFragment(new AddContactFragment());
                    mFab.hide();
                    mFab.setEnabled(false);
                }

            });
            //Requests/Invitations has been chosen
        }

        DrawerLayout drawer = findViewById(R.id.activity_messaging_container);
        drawer.closeDrawer(GravityCompat.START);


        return true;
    }

    @Override
    public void onWeatherListFragmentInteraction (DailyWeather weather) {

    }

    @Override
    public void onHourlyListFragmentInteraction (HourlyWeather weather) {

    }


    private void handleAddLocationOnPostExecute(String s) {
        JSONObject resultJSON = null;
        try {
            resultJSON = new JSONObject(s);
            boolean success = resultJSON.getBoolean("success");
            if (success) {
                onWaitFragmentInteractionHide();
            } else {
                Log.e("WEB SERVICE","Web Service returned false.");
                onWaitFragmentInteractionHide();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    /**
     * Helper method used to handle the tasks after the async task has been completed for receiving contact list.
     *
     * @param result String which represents the JSON result.
     */
    private void handleLocationGetOnPostExecute(final String result) {
        //parse JSON
        try {

            onWaitFragmentInteractionHide();
            JSONObject resultJSON = new JSONObject(result);
            boolean success = resultJSON.getBoolean("success");




            if (success) {

                mLocation = new ArrayList<>();
                JSONArray locationArray = resultJSON.getJSONArray( "nickname" );
                JSONArray latitudeArray = resultJSON.getJSONArray( "latitude" );
                JSONArray longitudeArray = resultJSON.getJSONArray( "longitude" );
                JSONArray zipArray = resultJSON.getJSONArray( "zip" );

                Log.e("LATITUDE!!!!", longitudeArray.toString());
//
//                System.out.println("EMAILS!!!" + emails.get(0).toString());
                for (int i = 0; i < locationArray.length(); i++) {
                    location loc = new location.Builder(locationArray.get(i).toString())
                            .addNickname( locationArray.get(i).toString())
                            .addCity( latitudeArray.get(i).toString() )
                            .addState( longitudeArray.get(i).toString() )
                            .addzip( zipArray.get(i).toString() )
                            .build();

                    mLocation.add(loc);

                }
                SavedLocationFragment savedLocationFragment = new SavedLocationFragment();
//                //Bundle list of connections as arguments and load connection fragment
//                Connection[] connectionsAsArray = new Connection[mConnections.size()];
//                connectionsAsArray = mConnections.toArray(connectionsAsArray);
                location[] locationAsArray = new location[mLocation.size()];

                locationAsArray = mLocation.toArray(locationAsArray);
//                //Bundle connections and send as arguments
                Bundle args = new Bundle();
                args.putSerializable(savedLocationFragment.ARG_LOCATION_LIST, locationAsArray);
                args.putSerializable("change", false);
                Fragment frag = new SavedLocationFragment();
                frag.setArguments( args );
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

    private void handleWeatherPostExecute(final String response) {

        try {
            mDailyWeather = new ArrayList<>();
            JSONObject result = new JSONObject(response);
            JSONArray listArray = result.getJSONArray("list");
            for (int i = 0; i < listArray.length(); i ++) {
                JSONObject jsonWeather = listArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0);
                JSONObject jsonTemp = listArray.getJSONObject(i).getJSONObject("main");
                double max = convertKtoF(jsonTemp.getInt("temp_max"));
                double min = convertKtoF(jsonTemp.getInt("temp_min"));

                mDailyWeather.add(new DailyWeather.Builder(
                        jsonWeather.getString("main"))
                        .addIcon(jsonWeather.getString("icon"))
                        .addHighTemp(max)
                        .addLowTemp(min)
                        .build());
            }

            new GetAsyncTask.Builder("https://api.weatherbit.io/v2.0/forecast/hourly?postal_code=" + mZip +"&country=US&key=723794c0a4a547688c417ccca5784221&hours=24")//uri.toString()
                    .onPreExecute(this::onWaitFragmentInteractionShow)
                    .onPostExecute(this::handleHourlyWeatherPostExecute)
                    .onCancelled(this::handleErrorsInTask)
                    .build()
                    .execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * helper function to convert kelvin to Fahrenheit
     * @param k
     * @return
     */
    private double convertKtoF(double k) {
        return  Math.round((k*1.8 - 459.67) * 100) / 100;
    }

    private void handleHourlyWeatherPostExecute(final String response) {

        try {
            List<HourlyWeather> hourlyWeathers = new ArrayList<>();
            JSONObject result = new JSONObject(response);
            JSONArray data = result.getJSONArray("data");

            for (int i = 0; i < data.length(); i ++) {
                JSONObject jsonWeather = data.getJSONObject(i).getJSONObject("weather");
                double temp =convertCtoF( data.getJSONObject(i).getDouble("temp"));
                String time = data.getJSONObject(i).getString("timestamp_local");
                hourlyWeathers.add(new HourlyWeather.Builder(
                        jsonWeather.getString("description"))
                        .setTemp(temp)
                        .setTime(time)
                        .build());
            }
            DailyWeather[] dailyWeathersArray = new DailyWeather[mDailyWeather.size()];
            dailyWeathersArray = mDailyWeather.toArray(dailyWeathersArray);
//
            HourlyWeather[] hourlyWeathersArray = new HourlyWeather[hourlyWeathers.size()];
            hourlyWeathersArray = hourlyWeathers.toArray(hourlyWeathersArray);
            Fragment fragment = new WeatherHome();
            Bundle args = new Bundle();
            args.putSerializable("zip", mZip);
            args.putSerializable(DailyWeatherFragment.ARG_DAILYWEATHER_LIST, dailyWeathersArray);
            args.putSerializable(HourlyWeatherFragment.ARG_HOURLYWEATHER_LIST, hourlyWeathersArray);
            fragment.setArguments(args);
//
//            onWaitFragmentInteractionHide();
            loadFragment(fragment);
        } catch (JSONException e){
            e.printStackTrace();
        }
        onWaitFragmentInteractionHide();
    }

    private double convertCtoF(double c) {
        return Math.round((c*1.8+32) *100) / 100;
    }




    /**
     * Helper method used to handle the tasks after the async task has been completed for receiving contact list.
     *
     * @param result String which represents the JSON result.
     */
    private void handleLocationGetPostExecute(final String result) {
        //parse JSON
        try {

            onWaitFragmentInteractionHide();
            JSONObject resultJSON = new JSONObject(result);
            boolean success = resultJSON.getBoolean("success");




            if (success) {

                mLocation = new ArrayList<>();
                JSONArray locationArray = resultJSON.getJSONArray( "nickname" );
                JSONArray latitudeArray = resultJSON.getJSONArray( "latitude" );
                JSONArray longitudeArray = resultJSON.getJSONArray( "longitude" );
                JSONArray zipArray = resultJSON.getJSONArray( "zip" );

                Log.e("LATITUDE!!!!", longitudeArray.toString());
//
//                System.out.println("EMAILS!!!" + emails.get(0).toString());
                for (int i = 0; i < locationArray.length(); i++) {
                    location loc = new location.Builder(locationArray.get(i).toString())
                            .addNickname( locationArray.get(i).toString())
                            .addCity( latitudeArray.get(i).toString() )
                            .addState( longitudeArray.get(i).toString() )
                            .addzip( zipArray.get(i).toString() )
                            .build();

                    mLocation.add(loc);

                }
                SavedLocationFragment savedLocationFragment = new SavedLocationFragment();
//                //Bundle list of connections as arguments and load connection fragment
//                Connection[] connectionsAsArray = new Connection[mConnections.size()];
//                connectionsAsArray = mConnections.toArray(connectionsAsArray);
                location[] locationAsArray = new location[mLocation.size()];

                locationAsArray = mLocation.toArray(locationAsArray);
//                //Bundle connections and send as arguments
                Bundle args = new Bundle();
                args.putSerializable(savedLocationFragment.ARG_LOCATION_LIST, locationAsArray);
                args.putSerializable("change", true);
                Fragment frag = new SavedLocationFragment();
                frag.setArguments( args );
                onWaitFragmentInteractionHide();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                        .replace(R.id.changeLocation_container, frag);
                transaction.commit();

                mFab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (mCurrentLocation == null) {

                            Snackbar.make(view, "Please wait for location to enable", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show(); } else {
                            Intent i = new Intent(MessagingHomeActivity.this, MapsActivity.class);
                            //pass the current location on to the MapActivity when it is loaded
                            i.putExtra("LOCATION", mCurrentLocation);

                            startActivity(i);
                        }

                    }

                });


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
        Log.e("Deleting", msg);
        String msg2 = item.getEmail();

        JSONObject json = new JSONObject();
        JSONObject json2 = new JSONObject();
        try {

            json.put("email", msg);
            json.put("email2", msg2);

            json.put("email2", msg);
            json.put("email", msg2);


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
//        mFab.setImageResource(android.R.drawable.ic_input_add);


        uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_getContacts))
                .build();
        //handleConnectionGetInfoOnPostExecute
        msg = getIntent().getExtras().getString("email");
        Credentials creds = new Credentials.Builder(msg).build();
        getSupportActionBar().setTitle("Connections");
        new SendPostAsyncTask.Builder(uri.toString(),creds.asJSONObject())
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleConnectionGetOnPostExecute)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(this._refreshReceiver);
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
//        mFab.setImageResource(android.R.drawable.ic_input_add);

    }

    @Override
    public void onMessageListFragmentInteraction(Message item) {

        Fragment chatFrag;
        chatFrag = new ChatFragment2();

        Bundle args = new Bundle();

//        mEmail = getArguments().getString("email_token_123");
//        mJwToken = getArguments().getString("jwt_token");
//        mChatID = getArguments().getString("chat_id");
        Log.e("CHATID", "it is: " + item.getChatid());
        mChatId = item.getChatid();
        args.putString("chat_id", item.getChatid());
        args.putString("email_token_123", getIntent().getExtras().getString("email"));
        args.putString("jwt_token", getIntent().getExtras().getString(getString(R.string.keys_intent_jwt)));
        mFab.setEnabled(false);
        mFab.hide();

        chatFrag.setArguments(args);
        loadFragment(chatFrag);
    }

    @Override
    public void onMessageListRemoveFragmentInteraction(Message item) {
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_removeChat))
                .build();

        String chatId = item.getChatid();
        JSONObject json = new JSONObject();
        try {

            json.put("chatid", chatId);
            json.put("email", getIntent().getExtras().getString("email"));
        } catch (JSONException e) {
            Log.wtf("CREDENTIALS", "Error creating JSON: " + e.getMessage());
        }
        new SendPostAsyncTask.Builder(uri.toString(), json)
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleChatRemoveOnPostExecute)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
    }

    private void handleChatRemoveOnPostExecute(String s) {

        getSupportActionBar().setTitle("Chat");
        mFab.setEnabled(true);
        mFab.show();
//        mFab.setImageResource(android.R.drawable.ic_input_add);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new AddChatFragment());
                mFab.hide();
                mFab.setEnabled(false);
            }

        });
        loadChats();
        //Connections has been chosen
        onWaitFragmentInteractionHide();
    }


    /**
     * Method listening for change password button to be clicked.
     */
    @Override
    public void onChangePasswordClicked() {
        getSupportActionBar().setTitle("Messaging");
        loadChats();
    }

    private void handleLoadHomeOnPost (final String result){
        //parse JSON
        try {
            onWaitFragmentInteractionHide();
            System.out.println("2------_>");
            JSONObject resultJSON = new JSONObject(result);
            boolean success = resultJSON.getBoolean("success");


            if (success) {

                mConnections = new ArrayList<>();
                JSONArray firstnames = resultJSON.getJSONArray("firstnames");
                JSONArray lastnames = resultJSON.getJSONArray("lastnames");
                JSONArray usernames = resultJSON.getJSONArray("usernames");
                JSONArray emails = resultJSON.getJSONArray("emails");


                for (int i = 0; i < firstnames.length(); i++) {
                    Connection conn = new Connection.Builder(emails.get(i).toString())
                            .addFirst(firstnames.get(i).toString()).addLast(lastnames.get(i).toString())
                            .addUsername(usernames.get(i).toString()).build();
                    mConnections.add(conn);

                }

                //Bundle list of connections as arguments and load connection fragment

                mConnectionsAsArray = new Connection[mConnections.size()];
                mConnectionsAsArray = mConnections.toArray(mConnectionsAsArray);

                //load home after connections setup
                Fragment homeF = new HomeFragment();
                Bundle args = new Bundle();
                args.putSerializable("zip", mZip);
                args.putSerializable(ConnectionsFragment.ARG_CONNECTION_LIST, mConnectionsAsArray);
                homeF.setArguments(args);
                loadFragment(homeF);
            }
        }
        catch (Exception e) {

        }

    }

    /**
     * Helper method used to handle the tasks after the async task has been completed for receiving contact list.
     *
     * @param result String which represents the JSON result.
     */
    private void handleConnectionGetOnPostExecute(final String result) {
        //parse JSON
        try {
            onWaitFragmentInteractionHide();
            System.out.println("2------_>");
            JSONObject resultJSON = new JSONObject(result);
            boolean success = resultJSON.getBoolean("success");



            if (success) {

                mConnections = new ArrayList<>();
                JSONArray firstnames = resultJSON.getJSONArray("firstnames");
                JSONArray lastnames = resultJSON.getJSONArray("lastnames");
                JSONArray usernames = resultJSON.getJSONArray("usernames");
                JSONArray emails = resultJSON.getJSONArray("emails");


                for (int i = 0; i < firstnames.length(); i++) {
                    Connection conn = new Connection.Builder(emails.get(i).toString())
                            .addFirst(firstnames.get(i).toString()).addLast(lastnames.get(i).toString())
                            .addUsername(usernames.get(i).toString()).build();
                    mConnections.add(conn);

                }
                //Bundle list of connections as arguments and load connection fragment

                mConnectionsAsArray = new Connection[mConnections.size()];
                mConnectionsAsArray = mConnections.toArray(mConnectionsAsArray);
                //Bundle connections and send as arguments
                Bundle args = new Bundle();
                args.putSerializable(ConnectionsFragment.ARG_CONNECTION_LIST, mConnectionsAsArray);

                Fragment frag = new ConnectionInviteFragment();
                loadFragment(frag);
                frag = new ConnectionsFragment();
//                args.putString("passemail", email);
                frag.setArguments(args);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_messaging_connection_container, frag )
                        .addToBackStack(null);
                transaction.commit();

                Log.e("super!!!!!", "yup");

                if (mConnections.isEmpty()) {
                    mConnectionsAsArray = new Connection[mConnections.size()];
                    mConnectionsAsArray = mConnections.toArray(mConnectionsAsArray);
                    //Bundle connections and send as arguments
                    args = new Bundle();
                    System.out.println("4------_>");
                    args.putSerializable(ConnectionsFragment.ARG_CONNECTION_LIST, mConnectionsAsArray);
                    frag = new ConnectionInviteFragment();
                    loadFragment(frag);
                    frag = new ConnectionsFragment();
//                args.putString("passemail", email);
                    frag.setArguments(args);
                    transaction = getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_messaging_connection_container, frag )
                            .addToBackStack(null);
                    transaction.commit();
                }

                mEmails = new ArrayList<>();
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


        //onWaitFragmentInteractionHide();
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

            Log.e("CHATID", "it is: " + mChats.get(i));

            m[i] = new Message.Builder(mChatNames.get(mChats.get(i))).addUsers(("" + members.toString())).setChatID(mChats.get(i)).build();
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
            JSONArray users = resultJSON.getJSONArray("users");


            if (success) {
                mChats = new ArrayList<>();
                mPeople = new HashMap<>();
                mChatNames = new HashMap<>();
                mChatMembers = ArrayListMultimap.create();
                //mchats, mchatmembers, mpeople

                for (int i = 0; i < users.length(); i++) {
                    JSONObject user = users.getJSONObject(i);
                    String chatid = user.getString("chatid");
                    String memberid = user.getString("memberid");
                    String username = user.getString("username");
                    String chatname = user.getString("chatname");

                    if (!mChats.contains(chatid)) {
                        mChats.add(chatid);
                        mChatNames.put(chatid, chatname);
                    }

                    if (!mPeople.containsKey(memberid)) mPeople.put(memberid, username);
                    mChatMembers.put(chatid, memberid);
                }


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
        FragmentManager fragmentManager = getSupportFragmentManager();
        // this will clear the back stack and displays no animation on the screen
        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_messaging_container, frag)
                .addToBackStack(null);
        transaction.commit();


//        //Show FAB
//        mFab.setEnabled(true);
//        mFab.show();
        //mFab.setImageResource(android.R.drawable.ic_input_add);
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





    private void handleInvitationGetOnPostExecute(String result) {

        //parse JSON
        try {
            mEmails = new ArrayList<>();
            mConnections = new ArrayList<>();
            JSONObject resultJSON = new JSONObject(result);
            boolean success = resultJSON.getBoolean("success");
            JSONArray data = resultJSON.getJSONArray("message");

            if (success) {
                String email = getIntent().getExtras().getString(("email"));
                System.out.println(email+"  nav_request invitaions");
                onWaitFragmentInteractionHide();
                mEmails.clear();
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
                args.putString("passemail", email);
                System.out.println("before calling inv/req "+email);


                Fragment frag = new InvitationsFragment();

                frag.setArguments(args);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
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
                .appendPath(getString(R.string.ep_acceptcontact))
                .build();
        String msg = getIntent().getExtras().getString("email");
        String email2 = item.getEmail();
        JSONObject json = new JSONObject();
        try {
            json.put("email", msg);
            json.put("email2", email2);


        } catch (JSONException e) {
            Log.wtf("CREDENTIALS", "Error creating JSON: " + e.getMessage());
        }
        getSupportActionBar().setTitle("Invitations");
        new SendPostAsyncTask.Builder(uri.toString(),json)
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleInvitationAcceptOnPostExecute)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
        uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_accept_invitation))
                .build();
        json = new JSONObject();
        try {
            json.put("email", msg);
            json.put("email2", email2);
            json.put("accept","true");
        } catch (JSONException e) {
            Log.wtf("CREDENTIALS", "Error creating JSON: " + e.getMessage());
        }
        new SendPostAsyncTask.Builder(uri.toString(), json)
                .build().execute();
    }

    private void handleInvitationAcceptOnPostExecute(String result) {
        //parse JSON
        try {
            onWaitFragmentInteractionHide();
            mEmails = new ArrayList<>();
            mConnections = new ArrayList<>();
            JSONObject resultJSON = new JSONObject(result);
            boolean success = resultJSON.getBoolean("success");
            onWaitFragmentInteractionHide();
            if (success) {
                onWaitFragmentInteractionHide();

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
                getSupportActionBar().setTitle("Connections");
                new SendPostAsyncTask.Builder(uri.toString(),creds.asJSONObject())
                        .onPreExecute(this::onWaitFragmentInteractionShow)
                        .onPostExecute(this::handleConnectionGetOnPostExecute)
                        .onCancelled(this::handleErrorsInTask)
                        .build().execute();
                //Show FAB





                mFab.setEnabled(true);
                mFab.show();
//                mFab.setImageResource(android.R.drawable.ic_input_add);


                //Set on click listener for FAB
                mFab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        loadFragment(new AddContactFragment());
                        mFab.hide();
                        mFab.setEnabled(false);
                    }

                });


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

        uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_accept_invitation))
                .build();
        JSONObject json = new JSONObject();
        try {
            json.put("email", msg);
            json.put("email2", item.getEmail());
            json.put("accept","false");
        } catch (JSONException e) {
            Log.wtf("CREDENTIALS", "Error creating JSON: " + e.getMessage());
        }
        new SendPostAsyncTask.Builder(uri.toString(), json)
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
    public void onLocationListFragmentInteraction(location item) {


            mZip = Integer.parseInt(item.getZip());
            WeatherHome weatherHome = new WeatherHome();
            Bundle args = new Bundle();
            args.putSerializable("zip", mZip);
            weatherHome.setArguments(args);
            getSupportActionBar().setTitle("Weather Home");
            mFab.hide();
            mFab.setEnabled(false);

            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath("api.openweathermap.org/data/2.5/forecast?zip=" + mZip + "&cnt=10&appid=b0ce6ca6ee362ce9ea5bbe361fdcbf92")
                    .build();

            new GetAsyncTask.Builder("https://api.openweathermap.org/data/2.5/forecast?zip=" + mZip + "&cnt=10&appid=b0ce6ca6ee362ce9ea5bbe361fdcbf92")//uri.toString()
//                    .onPreExecute(this::onWaitFragmentInteractionShow)
                    .onPostExecute(this::handleWeatherPostExecute)
                    .onCancelled(this::handleErrorsInTask)
                    .build()
                    .execute();


            mFab.show();
//            mFab.setImageResource(android.R.drawable.ic_menu_save);
            mFab.setEnabled(true);




    }

    @Override
    public void onLocationListRemoveFragmentInteraction(location item) {
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_removeLocation))
                .build();

        String zip = item.getZip();
        String email = getIntent().getExtras().getString("email");
        JSONObject json = new JSONObject();
        try {

            json.put("email", email);
            json.put("zip", zip);

        } catch (JSONException e) {
            Log.wtf("CREDENTIALS", "Error creating JSON: " + e.getMessage());
        }
        new SendPostAsyncTask.Builder(uri.toString(), json)
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleLocationRemoveOnPostExecute)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
    }

    private void handleLocationRemoveOnPostExecute(String s) {
        getSupportActionBar().setTitle("Saved Locations");
        SavedLocationFragment locationFragment = new SavedLocationFragment();
        mLocation = new ArrayList<>();

        //Build ASNC task to grab connections from web service.
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_getLocation))
                .build();
        //handleConnectionGetInfoOnPostExecute
        String msg = getIntent().getExtras().getString("email");
        Credentials creds = new Credentials.Builder(msg).build();
//            getSupportActionBar().setTitle("Connections");
        new SendPostAsyncTask.Builder(uri.toString(),creds.asJSONObject())
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleLocationGetOnPostExecute)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();


        mFab.hide();
        mFab.setEnabled(false);
        onWaitFragmentInteractionHide();
    }


    @Override
    public void addToChatButton(Credentials credentials) {

        Bundle args = new Bundle();

        args.putString("chatid", credentials.getChatId());

        Fragment addToChat = new AddToChatFragment();
        addToChat.setArguments(args);
        loadFragment(addToChat);
    }

    @Override
    public void removeFromChatButton(Credentials credentials) {
        Bundle args = new Bundle();

        args.putString("chatid", credentials.getChatId());

        Fragment addToChat = new RemoveFromChatFragment();
        addToChat.setArguments(args);
        loadFragment(addToChat);

    }

    @Override
    public void addToChat(Credentials credentials) {

        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_addToChat))
                .build();
        JSONObject messageJson = new JSONObject();
        //Build message for web service.
        try {
            messageJson.put("email", credentials.getEmail());
            messageJson.put("chatid", credentials.getChatId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new SendPostAsyncTask.Builder(uri.toString(),messageJson)
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleAddToChatOnPostExecute)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();


    }

    private void handleAddToChatOnPostExecute(String result) {

        JSONObject resultJSON = null;
        try {
            resultJSON = new JSONObject(result);
            boolean success = resultJSON.getBoolean("success");

            if (success) {
                Fragment chatFrag;
                chatFrag = new ChatFragment2();

                Bundle args = new Bundle();

//              mEmail = getArguments().getString("email_token_123");
//              mJwToken = getArguments().getString("jwt_token");
//              mChatID = getArguments().getString("chat_id");

                args.putString("chat_id", mChatId);
                args.putString("email_token_123", getIntent().getExtras().getString("email"));
                args.putString("jwt_token", getIntent().getExtras().getString(getString(R.string.keys_intent_jwt)));
                mFab.setEnabled(false);
                mFab.hide();

                chatFrag.setArguments(args);
                loadFragment(chatFrag);
                onWaitFragmentInteractionHide();
            } else {
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
    public void removeFromChat(Credentials credentials) {
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_removeFromChat))
                .build();
        JSONObject messageJson = new JSONObject();
        //Build message for web service.
        try {
            messageJson.put("email", credentials.getEmail());
            messageJson.put("chatid", credentials.getChatId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new SendPostAsyncTask.Builder(uri.toString(),messageJson)
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleAddToChatOnPostExecute)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();

    }

    @Override
    public void addChat(String chatName) {
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_addChat))
                .build();
        JSONObject messageJson = new JSONObject();

        String email = getIntent().getExtras().getString(("email"));

        //Build message for web service.
        try {
            messageJson.put("chatname", chatName);
            messageJson.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new SendPostAsyncTask.Builder(uri.toString(),messageJson)
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleAddChatOnPostExecute)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
    }

    private void handleAddChatOnPostExecute(String s) {
        loadChats();
        getSupportActionBar().setTitle("Chat");
        mFab.setEnabled(true);
        mFab.show();
//        mFab.setImageResource(android.R.drawable.ic_input_add);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new AddChatFragment());
                mFab.hide();
                mFab.setEnabled(false);
            }

        });

        onWaitFragmentInteractionHide();

        //Connections has been chosen

    }

    @Override
    public void onHomeFragmentInteraction(Uri uri) {

    }

    //@Override
    public void onChangeLocationSubmit(int zip) {
        mZip = zip;

        WeatherHome weatherHome = new WeatherHome();
        Bundle args = new Bundle();
        args.putSerializable("zip", mZip);
        weatherHome.setArguments(args);
        getSupportActionBar().setTitle("Weather Home");
        mFab.hide();
        mFab.setEnabled(false);

        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath("api.openweathermap.org/data/2.5/forecast?zip=" + mZip + "&cnt=10&appid=b0ce6ca6ee362ce9ea5bbe361fdcbf92")
                .build();

        new GetAsyncTask.Builder("https://api.openweathermap.org/data/2.5/forecast?zip=" + mZip + "&cnt=10&appid=b0ce6ca6ee362ce9ea5bbe361fdcbf92")//uri.toString()
//                    .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleWeatherPostExecute)
                .onCancelled(this::handleErrorsInTask)
                .build()
                .execute();


        mFab.show();
//            mFab.setImageResource(android.R.drawable.ic_menu_save);
        mFab.setEnabled(true);

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
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("in push message receive---testingte+++++->MessagingHomeActivity--->>>>>><<<."+intent.toString());
            if(intent.hasExtra("SENDER") && intent.hasExtra("MESSAGE")) {
                String sender = intent.getStringExtra("SENDER");
                String messageText = intent.getStringExtra("MESSAGE");
//                mMessageOutputTextView.append(sender + ":" + messageText);
//                mMessageOutputTextView.append(System.lineSeparator());
//                mMessageOutputTextView.append(System.lineSeparator());
            }
        }
    }
}
