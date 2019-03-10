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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

//mMap.setMyLocationEnabled(  );
        mCurrentLocation = (Location) getIntent().getParcelableExtra("LOCATION");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//        checkLocationPermission();


        //
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_messaging_fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mapFragment.getMapAsync(new OnMapReadyCallback() {
//                    @Override
//                    public void onMapReady(GoogleMap googleMap) {
//                        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
//
//                        googleMap.addMarker(new MarkerOptions()
//                                .position(new LatLng(37.4233438, -122.0728817))
//                                .title("LinkedIn")
//                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
//
//                        googleMap.addMarker(new MarkerOptions()
//                                .position(new LatLng(37.4629101,-122.2449094))
//                                .title("Facebook")
//                                .snippet("Facebook HQ: Menlo Park"));
//
//                        googleMap.addMarker(new MarkerOptions()
//                                .position(new LatLng(37.3092293, -122.1136845))
//                                .title("Apple"));
//
//                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.4233438, -122.0728817), 10));
//                    }
//                });
//            }
//        });
        //
    }
//
public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
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
    //

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
//        UiSettings.setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled( true );
        mMap.getUiSettings().setMyLocationButtonEnabled( true );
        mMap.getUiSettings().setCompassEnabled( true );
//        mMap.getUiSettings().setMyL
//        googleMap.setMyLocationEnabled( true );
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
//
//        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        //
        mMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        System.out.println(latLng.longitude+"map clicked--------"+latLng.latitude);

        DecimalFormat df = new DecimalFormat( "#.#####" );
        mLong = Double.parseDouble(df.format(latLng.longitude));
        df = new DecimalFormat( "#.#######" );
        mLat = Double.parseDouble(df.format(latLng.latitude));

        System.out.println(mLong+"#####"+mLat);
//        mCurrentLocation.setLatitude( latLng.longitude );
        System.out.println(mCurrentLocation.getLongitude()+"map clicked+++++++"+mCurrentLocation.getLatitude());
//        Location loc = new Location(  )
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(mLat, mLong, 1);
//            List<Address> addresses = geocoder.getFromLocation(latLng.longitude, latLng.latitude, 1);

            if(!(addresses.get(0).getPostalCode().toString()).equals(null)) {
                mZipcode = Integer.parseInt( addresses.get( 0 ).getPostalCode().toString() );
                System.out.println( "-------ZIPPP----" + mZipcode );

                Intent intent = new Intent("zipCodeSent");
                intent.putExtra("zip", mZipcode);
                sendBroadcast(intent);
                finish();

            }else{
                System.out.println("cannot get zip code");
            }
        } catch (IOException e) {
            e.printStackTrace();
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
                System.out.println("marker clicked----");
//                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
//                builder.setTitle("Delete Marker");
//                builder.setMessage("Do you want to delete the marker?");
//
//                AlertDialog dialog = builder.create();
//                dialog.show();
                return true;
            }
        });
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f));

    }
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

//    @Override
//    public boolean onMarkerClick(Marker marker) {
//        System.out.println("marker clicked-------");
//        new AlertDialog.Builder(this)
//                .setTitle("Delete entry")
//                .setMessage("Are you sure you want to delete this entry?")
//
//                // Specifying a listener allows you to take an action before dismissing the dialog.
//                // The dialog is automatically dismissed when a dialog button is clicked.
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
//
//                // A null listener allows the button to dismiss the dialog and take no further action.
//                .setNegativeButton(android.R.string.no, null)
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .create()
//                .show();
//        return false;
//    }
}
