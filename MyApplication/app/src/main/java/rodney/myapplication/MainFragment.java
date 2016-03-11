package rodney.myapplication;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by rodney on 3/6/2016.
 */
public class MainFragment extends Fragment {

    private Button startButton;
    private Button previousTimesButton;
    private Button loginButton;
    private Button aboutButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_main, container, false);

        Typeface xenotronFont = Typeface.createFromAsset(getActivity().getAssets(), "Xenotron.ttf");

        ImageView startImage = (ImageView) v.findViewById(R.id.startImage);
        startImage.setScaleType(ImageView.ScaleType.FIT_XY);

        startButton = (Button) v.findViewById(R.id.startButton);
        startButton.setTypeface(xenotronFont);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StartActivity.class);
                startActivity(intent);
//                updateFragment(new StartFragment());
            }
        });

        previousTimesButton = (Button) v.findViewById(R.id.previousTimesButton);
        previousTimesButton.setTypeface(xenotronFont);

        previousTimesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PreviousTimesActivity.class);
                startActivity(intent);
           }
        });

        return v;
    }

    private void updateFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }
}
