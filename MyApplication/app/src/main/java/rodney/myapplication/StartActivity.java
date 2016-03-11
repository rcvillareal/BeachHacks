package rodney.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by rodney on 3/8/2016.
 */
public class StartActivity extends AppCompatActivity implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient client;
    private long startTime = 0;
    private TextView hourText;
    private TextView minuteText;
    private TextView secondText;
    private TextView centiText;
    private Button startTimer;
    private Button stopTimer;
    private Button saveButton;
    private Button checkButton;

    private GoogleMap mMap;
    private SupportMapFragment supportMapFragment;

    private LocationManager lm;
    private Location location;
    private android.location.LocationListener locationListener;

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
    private LatLng myLatLng;
    private LatLng destination;

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;

    private SharedPreferences prefs;
    private String prefVal;
    private Set<String> prefSet = new HashSet<String>();

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PowerManager mgr = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
        wakeLock.acquire();

        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .setAccountName("MyApp")
                .build();

        Log.d("CHECK", "CONNECTED? --------- " + mGoogleApiClient.isConnected());

        setContentView(R.layout.start);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs = getSharedPreferences("savedtimes-sp", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putStringSet("savedtimes-list", prefSet);
                editor.commit();
                Intent intent = new Intent(StartActivity.this, MainActivity.class);;
                startActivity(intent);
            }
        });

        Typeface xenotronFont = Typeface.createFromAsset(getAssets(), "Xenotron.ttf");
        Typeface bankGothicLightFont = Typeface.createFromAsset(getAssets(), "BankGothicLight.ttf");
        Typeface miniskapFont = Typeface.createFromAsset(getAssets(), "MINISKIP.TTF");

        hourText = (TextView) findViewById(R.id.hourText);
        hourText.setTypeface(miniskapFont);

        minuteText = (TextView) findViewById(R.id.minuteText);
        minuteText.setTypeface(miniskapFont);

        secondText = (TextView) findViewById(R.id.secondText);
        secondText.setTypeface(miniskapFont);

        centiText = (TextView) findViewById(R.id.centiText);
        centiText.setTypeface(miniskapFont);

        startTimer = (Button) findViewById(R.id.startTimerButton);
        startTimer.setTypeface(xenotronFont);
        startTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startMarker != null) {
                    startMarker.remove();
                }
                try {
                    if (mGoogleApiClient.isConnected()) {
                        startTime = System.currentTimeMillis();
                        timerHandler.postDelayed(timerRunnable, 0);
                        myLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                        startMarker = mMap.addMarker(new MarkerOptions().position(myLatLng).title("Start Location"));
                        start = true;
                    } else {
                        Toast toast = Toast.makeText(StartActivity.this, "Waiting for Google Client to Connect", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                } catch(Exception e) {
                    Toast toast = Toast.makeText(StartActivity.this, "Waiting for Google Client to Connect", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

            }
        });

        stopTimer = (Button) findViewById(R.id.stopTimerButton);
        stopTimer.setTypeface(xenotronFont);
        stopTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerHandler.removeCallbacks(timerRunnable);
                start = false;
            }
        });

        //not fully implemented as of yet
        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setTypeface(xenotronFont);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(millis > 0) {
                    prefVal = "" + millis;
//                    prefs = getSharedPreferences("savedtimes-sp", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = prefs.edit();
//                    editor.putLong("savedtimes-list", millis);
//                    editor.commit();
                    prefSet.add(prefVal);
                    Toast toast = Toast.makeText(StartActivity.this, "Saved", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(StartActivity.this, "Time cannot be 0", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

        checkButton = (Button) findViewById(R.id.checkButton);
        checkButton.setTypeface(xenotronFont);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startMarker != null) {
                    startMarker.remove();
                }
                try {
                    if (!(mGoogleApiClient.isConnected())) {
                        Toast toast = Toast.makeText(StartActivity.this, "Waiting for Google Client to Connect", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else {
                        Toast toast = Toast.makeText(StartActivity.this, "Getting Location", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        myLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(myLatLng).zoom(18).build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        startMarker = mMap.addMarker(new MarkerOptions().position(myLatLng).title("Start Location"));
                        Toast toast1 = Toast.makeText(StartActivity.this, "Click on your Destination", Toast.LENGTH_LONG);
                        toast1.setGravity(Gravity.CENTER, 0, 0);
                        toast1.show();
                    }
                } catch (Exception e) {
                    Toast toast = Toast.makeText(StartActivity.this, "Waiting for Google Client to Connect", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });


        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                3);

        points = new ArrayList<LatLng>();

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
//                mMap.setMyLocationEnabled(true);
//                mMap.getUiSettings().setMyLocationButtonEnabled(true);

                Log.d("CHECK", "CONNECTED? --------- " + mGoogleApiClient.isConnected());

                mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng position) {
                        if (marker != null) {
                            marker.remove();
                        }
                        marker = mMap.addMarker(new MarkerOptions().position(position).title("Destination"));
                        destination = position;
                        Log.d("CHECK", "CONNECTED? --------- " + mGoogleApiClient.isConnected());
                    }
                });
            }
        });

//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//        locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                lastLocation = location;
//                googleMapRef.addMarker(new MarkerOptions().title("Current").position(new LatLng(location.getLatitude(), location.getLongitude())));
//                googleMapRef.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//
//            }
//        };

    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    2);
        } else {
            PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        updateUI();
        myLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        if(start) {
            points.add(myLatLng);
            redrawLine();
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())).zoom(18).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        if(mCurrentLocation.equals(destination)) {
            timerHandler.removeCallbacks(timerRunnable);
            start = false;
            prefs = getSharedPreferences("savedtimes-sp", Context.MODE_PRIVATE);
            //SharedPreferences.Editor editor = prefs.edit();
            prefs.edit().putLong("savedtimes-list", millis).apply();
            Toast toast = Toast.makeText(StartActivity.this, "Saved", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private void updateUI() {
        if (null != mCurrentLocation) {
            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());
            LatLng latLng = new LatLng (mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        } else {
        }
    }

    private void redrawLine(){
        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            options.add(point);
        }
        //     addMarker(); //add Marker in current position
        line = mMap.addPolyline(options); //add Polyline
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
        timerHandler.removeCallbacks(timerRunnable);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

}
