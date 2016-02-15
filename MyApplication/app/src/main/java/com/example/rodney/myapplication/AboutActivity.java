package com.example.rodney.myapplication;

import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.w3c.dom.Text;

/**
 * Created by rodney on 2/8/2016.
 */
public class AboutActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private TextView aboutText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        Typeface xenotronFont = Typeface.createFromAsset(getAssets(), "Xenotron.ttf");
        Typeface bankGothicLightFont = Typeface.createFromAsset(getAssets(), "BankGothicLight.ttf");

        TextView aboutTitle = (TextView) findViewById(R.id.aboutTitle);
        aboutTitle.setTypeface(xenotronFont);

        aboutText = (TextView) findViewById(R.id.aboutText);
        aboutText.setTypeface(bankGothicLightFont);
        setAboutText();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "About Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.rodney.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "About Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.rodney.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private void setAboutText() {
        String about = "ABOUT: This is a timer app that calculates the trip time from your initial "
                + "location to a selected destination. You can also set your destination as your "
                + "initial position to time \"laps\".\n"
                + "\nRecommended Applications (but not limited to):\n\nWALKING/RUNNING - "
                + "time your walks/runs. These times will be saved and can be used as a reference "
                + "to help improve your future times.\n\nCYCLING - same use as running. Time your "
                + "bike trips to help improve your cycling skills.\n\nAUTOMOTIVE - calculate your "
                + "trip times when commuting. Take alternate routes and check on average which route "
                + "is fastest. You can also use this app on your local track to time your laps.";
        aboutText.setText(about);
    }

}
