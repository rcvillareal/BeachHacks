package rodney.myapplication;

import android.support.v4.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import rodney.myapplication.R;
import com.google.android.gms.common.api.GoogleApiClient;
import org.w3c.dom.Text;

/**
 * Created by rodney on 2/8/2016.
 */
public class AboutFragment extends Fragment {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private TextView aboutText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.about, container, false);

        aboutText = (TextView) v.findViewById(R.id.aboutText);

//        Typeface xenotronFont = Typeface.createFromAsset(getActivity().getAssets(), "Xenotron.ttf");
        Typeface bankGothicLightFont = Typeface.createFromAsset(getActivity().getAssets(), "BankGothicLight.ttf");

//        TextView aboutTitle = (TextView) findViewById(R.id.aboutTitle);
//        aboutTitle.setTypeface(xenotronFont);

//        aboutText = (TextView) findViewById(R.id.aboutText);
        aboutText.setTypeface(bankGothicLightFont);
        setAboutText();

        return v;
    }

    private void setAboutText() {
        String about = "ABOUT: This is a timer app that calculates the trip time from your initial "
                + "location to a selected destination. You can also set your destination as your "
                + "initial position to time \"laps\"(future implementation).\n"
                + "\nRecommended Applications (but not limited to):\n\nWALKING/RUNNING - "
                + "time your walks/runs. These times will be saved and can be used as a reference "
                + "to help improve your future times.\n\nCYCLING - same use as running. Time your "
                + "bike trips to help improve your cycling skills.\n\nAUTOMOTIVE - calculate your "
                + "trip times when commuting. Take alternate routes and check on average which route "
                + "is fastest."
                + "\n\nNOTE: GPS and timing features are not fully "
                + "implemented ie. lap times, saving times and viewing times.\n";
        aboutText.setText(about);
    }
}
