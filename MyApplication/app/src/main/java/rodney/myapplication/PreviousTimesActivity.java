package rodney.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by rodney on 3/10/2016.
 */
public class PreviousTimesActivity extends AppCompatActivity {

    private ListView listView;
    private SupportMapFragment supportMapFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.previous_times);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {;
                Intent intent = new Intent(PreviousTimesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        listView = (ListView) findViewById(R.id.listView);
        refreshListView();

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

            }
        });

        refreshListView();
    }

    private void refreshListView() {
        String[] savedTimesList = readSavedTimesFromDb();
        ListAdapter adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, savedTimesList);
        listView.setAdapter(adapter);
        Log.i("Shared Pref data ----- ", "" + savedTimesList.length + "    ");
        for(int i = 0; i < savedTimesList.length; i++) {
            System.out.println("data " + i + " ------- " + savedTimesList[i]);
        }
    }

    private String[] readSavedTimesFromDb() {

        SharedPreferences prefs = getSharedPreferences("savedtimes-sp", Context.MODE_PRIVATE);

        Set<String> prefTimes = new HashSet<String>();
        prefTimes = prefs.getStringSet("savedtimes-list", null);

        String[] times = null;

        try {
            times = Arrays.copyOf(prefTimes.toArray(), prefTimes.size(), String[].class);
            Log.i("pre size ------ ", " " + prefTimes.size());
        } catch(Exception e) {
        }

        String[] convertedTimes = stringPrintConverter(times).clone();

        return convertedTimes;
    }

    private String[] stringPrintConverter(String[] values) {
        String[] converted = new String[values.length];

        for(int i = 0; i < values.length; i++) {
            long millis = Integer.parseInt(values[i]);
            int centiseconds = (int) millis/10;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            int hours = minutes / 60;
            minutes = minutes % 60;
            seconds = seconds % 60;
            centiseconds = centiseconds % 100;
            converted[i] = "Hr: " + hours + "     Min: " + minutes
                    + "     Sec: " + seconds + "     Cs: " + centiseconds;
        }
        return converted;
    }
}
