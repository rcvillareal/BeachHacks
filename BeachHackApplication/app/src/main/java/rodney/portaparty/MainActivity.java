package rodney.portaparty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
public class MainActivity extends AppCompatActivity{
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
        Firebase.setAndroidContext(this);
        firebase = new Firebase("https://portaparty.firebaseio.com/");
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

}