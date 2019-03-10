package ethanwc.tcss450.uw.edu.template.Messenger;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;

import ethanwc.tcss450.uw.edu.template.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Location mCurrentLocation;

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

        // Add a marker in the current device location and move the camera
        LatLng current = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(current).title("Current Location"));
        //Zoom levels are from 2.0f (zoomed out) to 21.f (zoomed in)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 15.0f));

        mMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        System.out.println("map clicked--------");
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f));

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
