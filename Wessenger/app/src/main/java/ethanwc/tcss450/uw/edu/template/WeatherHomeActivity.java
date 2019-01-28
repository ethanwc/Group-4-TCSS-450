package ethanwc.tcss450.uw.edu.template;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import ethanwc.tcss450.uw.edu.template.dummy.DummyContent;

public class WeatherHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SavedLocationFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_home);
        Toolbar toolbar = findViewById(R.id.toolbar_weather_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab_weather_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.activity_weather_container);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navview_weather_nav);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.activity_weather_container);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.weather_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_weather_home) {
            Intent intent = new Intent(WeatherHomeActivity.this, WeatherHomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_Change_Locations) {
            ChangeLocationsFragment changeFragment = new ChangeLocationsFragment();

            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_weather_container, changeFragment)
                    .addToBackStack(null);
            transaction.commit();

        } else if (id == R.id.nav_View_Saved_Location) {
            SavedLocationFragment locationFragment = new SavedLocationFragment();

            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_weather_container, locationFragment)
                    .addToBackStack(null);
            transaction.commit();

        } else if (id == R.id.nav_chat_home) {
            Intent intent = new Intent(WeatherHomeActivity.this, MessagingHomeActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_chat_view_connections) {
            ConnectionsFragment connectionsFragment = new ConnectionsFragment();
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_weather_container, connectionsFragment)
                    .addToBackStack(null);
            transaction.commit();

        } else if (id == R.id.nav_Request_Invitations) {
            InvitationsFragment invitationsFragment = new InvitationsFragment();
            //RequestsFragment requestsFragment = new RequestsFragment();
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()

                    .replace(R.id.fragment_weather_container, invitationsFragment)
                    //.replace(R.id.secondFragmentContainer, requestsFragment)
                    .addToBackStack(null);
            transaction.commit();
        }

        DrawerLayout drawer = findViewById(R.id.activity_weather_container);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void loadFragment(Fragment frag){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_weather_container, frag )
                .addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
