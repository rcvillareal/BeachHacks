package rodney.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import rodney.myapplication.R;
import com.google.android.gms.common.api.GoogleApiClient;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by rodney on 2/8/2016.
 */

public class PreviousTimesFragment extends Fragment {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    private ListView listView;
    private SupportMapFragment supportMapFragment;
    private GoogleApiClient client;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.previous_times, container, false);

//        Typeface xenotronFont = Typeface.createFromAsset(getAssets(), "Xenotron.ttf");
        Typeface bankGothicLightFont = Typeface.createFromAsset(getActivity().getAssets(), "BankGothicLight.ttf");

//        TextView previousTimesText = (TextView) findViewById(R.id.previousTimesTitle);
//        previousTimesText.setTypeface(xenotronFont);

//        TextView timesText = (TextView) v.findViewById(R.id.timesText);
//        timesText.setTypeface(bankGothicLightFont);

        listView = (ListView) v.findViewById(R.id.listView);

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

            }
        });

//        refreshListView();

        return v;
    }

//    private void refreshListView() {
//        List savedTimesList = readSavedTimesFromDb();
//        listView.setAdapter(new ArrayAdapter(
//                getContext(), android.R.layout.simple_list_item_1, savedTimesList
//        ));
//    }
//
//    private List readSavedTimesFromDb() {
//        List times = new ArrayList();
//        SharedPreferences prefs = getActivity().getSharedPreferences("savedtimes-sp", Context.MODE_PRIVATE);
//
//        Set<String> keys = prefs.keySet();
//
//        for(Map.Entry<String, ?> entry : keys.entrySet()) {
//            Log.d("Map values ", entry.getKey() + " - " + entry.getValue().toString());
//        }
//
//        long time = prefs.getLong("savedtimes-list", -1);
//        if(time != -1) {
//            times.add(0, time);
//        }
//        System.out.println("hiiiiiiiiii - " + prefs);
////        System.out.println("hiiiiiiiiii"+times.get(1));
//        return times;
//    }
}

