package rodney.myapplication;

import android.graphics.Color;
import android.location.Criteria;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import rodney.myapplication.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.content.SharedPreferences;

/**
 * Created by rodney on 2/8/2016.
 */
public class StartFragment extends Fragment {
    private TextView startTitle;
    private GoogleApiClient client;
    private long startTime = 0;
    private TextView hourText;
    private TextView minuteText;
    private TextView secondText;
    private TextView centiText;
    private Button startTimer;
    private Button stopTimer;
    private Button saveButton;

    private GoogleMap mMap;
    private SupportMapFragment supportMapFragment;
    private Map<String, Marker> keyMarkerMap = new HashMap<String, Marker>();

    private LocationManager locationManager;
    private Location lastLocation;
    private LocationListener locationListener;

    private int hours;
    private int minutes;
    private int seconds;
    private int centiseconds;
    private long millis;

    private boolean start = false;

    private Marker marker;
    private Marker startMarker;
    private ArrayList<LatLng> points;
    private Polyline line;


    Handler timerHandler = new Handler();

    Runnable timerRunnable = new Runnable() {
        @Override
        public void run () {
        millis = System.currentTimeMillis() - startTime;
        centiseconds = (int) millis/10;
        seconds = (int) (millis / 1000);
        minutes = seconds / 60;
        hours = minutes / 60;
        minutes = minutes % 60;
        seconds = seconds % 60;
        centiseconds = centiseconds % 100;

        hourText.setText(String.format("%d", hours));
        minuteText.setText(String.format("%02d", minutes));
        secondText.setText(String.format("%02d", seconds));
        centiText.setText(String.format("%02d", centiseconds));

        timerHandler.postDelayed(this, 10);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.start, container, false);

        Typeface xenotronFont = Typeface.createFromAsset(getActivity().getAssets(), "Xenotron.ttf");
        Typeface bankGothicLightFont = Typeface.createFromAsset(getActivity().getAssets(), "BankGothicLight.ttf");
        Typeface miniskapFont = Typeface.createFromAsset(getActivity().getAssets(), "MINISKIP.TTF");

        hourText = (TextView) v.findViewById(R.id.hourText);
        hourText.setTypeface(miniskapFont);

        minuteText = (TextView) v.findViewById(R.id.minuteText);
        minuteText.setTypeface(miniskapFont);

        secondText = (TextView) v.findViewById(R.id.secondText);
        secondText.setTypeface(miniskapFont);

        centiText = (TextView) v.findViewById(R.id.centiText);
        centiText.setTypeface(miniskapFont);

        startTimer = (Button) v.findViewById(R.id.startTimerButton);
        startTimer.setTypeface(xenotronFont);
        startTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = System.currentTimeMillis();
                timerHandler.postDelayed(timerRunnable, 0);

//                LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                LatLng latlng = new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());

                if(startMarker != null) {
                    startMarker.remove();
                }
                startMarker = mMap.addMarker(new MarkerOptions().position(latlng).title("Start Location"));
                start = true;
            }
        });

        stopTimer = (Button) v.findViewById(R.id.stopTimerButton);
        stopTimer.setTypeface(xenotronFont);
        stopTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerHandler.removeCallbacks(timerRunnable);
                start = false;
            }
        });

        //not implemented as of yet
        saveButton = (Button) v.findViewById(R.id.saveButton);
        saveButton.setTypeface(xenotronFont);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getActivity().getSharedPreferences("savedtimes-sp", Context.MODE_PRIVATE);
                //SharedPreferences.Editor editor = prefs.edit();
                prefs.edit().putLong("savedtimes-list", millis).apply();
            }
        });


//        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                lastLocation = location;
//                mMap.addMarker(new MarkerOptions().title("Current").position(new LatLng(location.getLatitude(), location.getLongitude())));
//                mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
//            }
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//            }
//            @Override
//            public void onProviderEnabled(String provider) {
//            }
//            @Override
//            public void onProviderDisabled(String provider) {
//            }
//        };



        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                3);

        points = new ArrayList<LatLng>();

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setMyLocationEnabled(true);
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED)
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);

                LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                LatLng mylatlng = new LatLng(location.getLatitude(), location.getLongitude());

                CameraPosition cameraPosition = new CameraPosition.Builder().target(mylatlng).zoom(18).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));



                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location location) {
                        if(start) {
                            LatLng latLng = new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
                            points.add(latLng);
                            redrawLine();
                        }
                    }

                });

//                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
//                    @Override
//                    public void onMyLocationChange(Location location) {
//                        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
//                        MarkerOptions markerOptions = new MarkerOptions()
//                                .position(latlng)
//                                .title("title")
//                                .snippet("some text in balloon");
//                        CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng).zoom(18).build();
//
//                        Marker markerFinal = mMap.addMarker(markerOptions);
//                        markerFinal.showInfoWindow();
//
//                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                    }
//                });

                mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

                    @Override
                    public void onMapLongClick(LatLng position) {
                        if (marker != null) {
                            marker.remove();
                        }
                        marker = mMap.addMarker(new MarkerOptions().position(position).title("Destination"));
                    }
                });
            }
        });



//        if (ContextCompat.checkSelfPermission(getActivity(),
//                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
//                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        ActivityCompat.requestPermissions(getActivity(),
//                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                3);
//        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap googleMap) {
//                googleMapRef = googleMap;
//                // Add a marker in Sydney, Australia, and move the camera.
//                LatLng current = googleMapRef.
//                LatLng sydney = new LatLng(-34, 151);
//                googleMapRef.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//                googleMapRef.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//            }
//        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        return v;
    }

    private void redrawLine(){

        mMap.clear();  //clears all Markers and Polylines

        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            options.add(point);
        }
   //     addMarker(); //add Marker in current position
        line = mMap.addPolyline(options); //add Polyline
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 3, locationListener);
    }
}

