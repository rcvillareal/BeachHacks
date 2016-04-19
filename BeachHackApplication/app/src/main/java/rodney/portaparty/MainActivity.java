package rodney.portaparty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rodney on 4/3/2016.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Firebase firebase;
    private Button addItemButton;
    private EditText itemEditText;
    private ListView listView;
    private Button createPartyButton;
    private String username;
    private CustomListViewAdapter customListViewAdapter;
    private ArrayList<HashMap<String,String>> arrayList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Firebase.setAndroidContext(this);
        firebase = new Firebase("https://portaparty.firebaseio.com/");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        addItemButton = (Button) findViewById(R.id.addItemButton);
        itemEditText = (EditText) findViewById(R.id.itemEditText);
        listView = (ListView) findViewById(R.id.item_list);
        createPartyButton = (Button) findViewById(R.id.createPartyButton);


        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        if(sharedPreferences.getString("username", "").isEmpty()){
            username = sharedPreferences.getString("username", "");
            Toast.makeText(MainActivity.this, username+"IN THE IF", Toast.LENGTH_SHORT).show();

        }else{
            createPartyButton.setEnabled(false);
            addItemButton.setEnabled(true);
            username = sharedPreferences.getString("username", "");
            Toast.makeText(MainActivity.this, username, Toast.LENGTH_SHORT).show();
            arrayList = popCustomListViewAdapter();

        }

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "You added "+ itemEditText.getText().toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    private ArrayList<HashMap<String,String>> popCustomListViewAdapter() {
        listView = (ListView) findViewById(R.id.item_list);
        final ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        firebase.child("party").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, String> data = new HashMap<>();

                for(DataSnapshot photoSnapshot : dataSnapshot.child(username+"/item").getChildren()){

                        data = new HashMap<>();
                        //data.put("title", childSnap.child("title").getValue().toString());
                        data.put("item", photoSnapshot.getValue().toString());
                       // data.put("date", childSnap.child("date").getValue().toString());
                        //data.put("answer_complete", childSnap.child("complete").getValue().toString());
                        //data.put("snapshotLocation","question/photo/"+childSnap.child("category")
                         //       .getValue().toString() + "/" + childSnap.child("title")
                         //       .getValue().toString() );
                       // data.put("photo", childSnap.child("photo").getValue().toString());

                        arrayList.add(data);
                }
//to add to git
                //Setup adapter
                customListViewAdapter = new CustomListViewAdapter(getApplicationContext(), arrayList);
                listView.setAdapter(customListViewAdapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });
        return arrayList;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.partiesItem) {

        } else if (id == R.id.loginItem) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.createAccountItem) {
            Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
            startActivity(intent);
        } else if (id == R.id.aboutItem) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.create_account);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
