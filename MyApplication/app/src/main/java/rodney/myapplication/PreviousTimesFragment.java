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
import android.widget.ListAdapter;
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
import java.util.HashSet;
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

//        listView = (ListView) v.findViewById(R.id.listView);
//
//        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap googleMap) {
//
//            }
//        });
//
//        refreshListView();

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (ListView) getActivity().findViewById(R.id.listView);
        refreshListView();

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

            }
        });


    }

    private void refreshListView() {
        String[] savedTimesList = readSavedTimesFromDb();
        ListAdapter adapter = new ArrayAdapter<String> (
                getActivity(), android.R.layout.simple_list_item_1, savedTimesList);
        listView.setAdapter(adapter);
    }

    private String[] readSavedTimesFromDb() {

        SharedPreferences prefs = getContext().getSharedPreferences("savedtimes-sp", Context.MODE_PRIVATE);

        Set<String> prefTimes = new HashSet<String>();
        prefTimes = prefs.getStringSet("savedtimes-list", null);

        String[] times = new String[prefTimes.size()];
        int i = 0;
        if(prefTimes != null) {
            for(String num: prefTimes) {
//                long l = Long.parseLong(num);
//                times.add(num);
                times[i] = num + " ms";
            }
        }
//        System.out.println("hiiiiiiiiii - " + times.get(0));
//        System.out.println("hiiiiiiiiii - " + times.get(1));
        return times;
    }
}

