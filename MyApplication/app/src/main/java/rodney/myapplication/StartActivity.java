package rodney.myapplication;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import rodney.myapplication.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by rodney on 2/8/2016.
 */
public class StartActivity extends AppCompatActivity {
    private TextView startTitle;
    private GoogleApiClient client;
    long startTime = 0;
    TextView hourText;
    TextView minuteText;
    TextView secondText;
    TextView centiText;
    Button startTimer;
    Button stopTimer;
    Button saveButton;
    Handler timerHandler = new Handler();

    Runnable timerRunnable = new Runnable() {
        @Override
        public void run () {
            long millis = System.currentTimeMillis() - startTime;
            int centis = (int) millis/10;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            int hours = minutes / 60;
            minutes = minutes % 60;
            seconds = seconds % 60;
            centis = centis % 100;

            hourText.setText(String.format("%d", hours));
            minuteText.setText(String.format("%02d", minutes));
            secondText.setText(String.format("%02d", seconds));
            centiText.setText(String.format("%02d", centis));

            timerHandler.postDelayed(this, 10);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        Typeface xenotronFont = Typeface.createFromAsset(getAssets(), "Xenotron.ttf");
        Typeface bankGothicLightFont = Typeface.createFromAsset(getAssets(), "BankGothicLight.ttf");
        Typeface miniskapFont = Typeface.createFromAsset(getAssets(), "MINISKIP.TTF");

        startTitle = (TextView) findViewById(R.id.startTitle);
        startTitle.setTypeface(xenotronFont);

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
                startTime = System.currentTimeMillis();
                timerHandler.postDelayed(timerRunnable, 0);
            }
        });

        stopTimer = (Button) findViewById(R.id.stopTimerButton);
        stopTimer.setTypeface(xenotronFont);
        stopTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerHandler.removeCallbacks(timerRunnable);
            }
        });

        //not implemented as of yet
        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setTypeface(xenotronFont);

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
                Uri.parse("android-app://rodney.myapplication/http/host/path")
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
                Uri.parse("android-app://rodney.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}

