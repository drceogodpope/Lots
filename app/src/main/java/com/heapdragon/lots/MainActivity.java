package com.heapdragon.lots;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSites();

    }


    public void addSite(String name,int numberOfLots){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = database.getReference();
        DatabaseReference sites_infoRef = rootRef.child("Sites_info").push();
        sites_infoRef.child("name").setValue(name);
        sites_infoRef.child("total_lots").setValue(numberOfLots);
    }

    public void getSites() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = database.getReference();
        DatabaseReference sites_infoRef = rootRef.child("Sites_info");
        sites_infoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "Something Changed Again");
                for(DataSnapshot child:dataSnapshot.getChildren()){
                    Log.d(TAG,child.getKey() + " : " + child.child("name").getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                String tits = "Shitty tits";
            }
        });
    }

}
