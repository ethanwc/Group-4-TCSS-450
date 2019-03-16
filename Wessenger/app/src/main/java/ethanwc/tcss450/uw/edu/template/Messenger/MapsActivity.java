package ethanwc.tcss450.uw.edu.template.Messenger;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import ethanwc.tcss450.uw.edu.template.Main.MainActivity;
import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.model.Credentials;
import ethanwc.tcss450.uw.edu.template.model.location;

import static ethanwc.tcss450.uw.edu.template.Main.MainActivity.EXTRA_MESSAGE;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {

    private GoogleMap mMap;
    private Location mCurrentLocation;
    private Double mLat;
    private Double mLong;
    private int mZipcode;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        mCurrentLocation = (Location) getIntent().getParcelableExtra("LOCATION");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }

    /**
     * Check if the location is permitted by device.
     * @return boolean
     */
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled( true );
        mMap.getUiSettings().setMyLocationButtonEnabled( true );
        mMap.getUiSettings().setCompassEnabled( true );

        //check for the permission to access the location to use on map
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }
        // Add a marker in the current device location and move the camera
        LatLng current = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(current).title("Current Location"));
        //Zoom levels are from 2.0f (zoomed out) to 21.f (zoomed in)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 15.0f));

//        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        //
        mMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        System.out.println(latLng.longitude+"map clicked--------"+latLng.latitude);

        // convert the float format of latitude and longitude from map
        DecimalFormat df = new DecimalFormat( "#.#####" );
        mLong = Double.parseDouble(df.format(latLng.longitude));
        df = new DecimalFormat( "#.#######" );
        mLat = Double.parseDouble(df.format(latLng.latitude));

        System.out.println(mLong+"#####"+mLat);
        System.out.println(mCurrentLocation.getLongitude()+"map clicked+++++++"+mCurrentLocation.getLatitude());

        // Class to convert the geo code into the zipcode
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {

            List<Address> addresses = geocoder.getFromLocation(mLat, mLong, 1);

            if(!(addresses.get(0).getPostalCode().toString()).equals(null)) {
                if(addresses.get( 0 ).getPostalCode().toString().equals(null)){
                    // if zipcode is unable default
                    mZipcode = 98418;
                    System.out.println( "-------ZIPPP----" + mZipcode );

                    Intent intent = new Intent( "zipCodeSent" );
                    intent.putExtra( "zip", mZipcode );
                    sendBroadcast( intent );
                    finish();
                }else {
                    mZipcode = Integer.parseInt( addresses.get( 0 ).getPostalCode().toString() );
                    System.out.println( "-------ZIPPP----" + mZipcode );

                    Intent intent = new Intent( "zipCodeSent" );
                    intent.putExtra( "zip", mZipcode );
                    sendBroadcast( intent );
                    finish();
                }
            }else{
                System.out.println("cannot get zip code");

            }
        } catch (IOException e) {
            e.printStackTrace();
// if zipcode is unable default
            mZipcode = 98418;
            System.out.println( "-------ZIPPP----" + mZipcode );

            Intent intent = new Intent( "zipCodeSent" );
            intent.putExtra( "zip", mZipcode );
            sendBroadcast( intent );
            finish();
        } catch(IndexOutOfBoundsException e){
            System.out.println("---index out exception---");
            // if zipcode is outside us
            mZipcode = 98418;
            System.out.println( "-------ZIPPP----" + mZipcode );

            Intent intent = new Intent( "zipCodeSent" );
            intent.putExtra( "zip", mZipcode );
            sendBroadcast( intent );
            finish();
        }  catch(NullPointerException e) {
            // if zipcode is outside us
            mZipcode = 98418;
            System.out.println( "-------ZIPPP----" + mZipcode );

            Intent intent = new Intent( "zipCodeSent" );
            intent.putExtra( "zip", mZipcode );
            sendBroadcast( intent );
            finish();
        }

        Log.d("LAT/LONG", latLng.toString());

        Marker marker = mMap.addMarker(new MarkerOptions() .position(latLng)
                .title("New Marker"));

        Random r = new Random();
        int num =  r.nextInt((4 - 1) + 1) + 1;
        if(num == 1) {
            marker.setIcon( BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_CYAN ) );
        } else if(num ==2) {
            marker.setIcon( BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_AZURE ) );
        } else if (num == 3) {
            marker.setIcon( BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_BLUE ) );
        }else {
            marker.setIcon( BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_GREEN ) );
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker m) {

                return true;
            }
        });
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f));

    }

    /**
     *
     * @param i, random value to select the color
     * @return, the color
     */
    public String color(int i){
        String color;
        switch(i){
            case 1:
                color = "HUE_AZURE";
                break;

            case 2:
                color = "HUE_CYAN";
                break;

            case 3:
                color = "HUE_GREEN";
                break;

            default:
                color = "HUE_RED";
                break;

        }
        return color;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
//        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
//        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

}
