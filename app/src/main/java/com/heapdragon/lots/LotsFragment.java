package com.heapdragon.lots;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import static com.heapdragon.lots.DataBaseConstants.LOTS_NODE_PREFIX;
import static com.heapdragon.lots.DataBaseConstants.LOTS_PRIMARY_STATUS_PREFIX;
import static com.heapdragon.lots.DataBaseConstants.LOTS_SECONDARY_STATUS_PREFIX;

public class LotsFragment extends android.support.v4.app.Fragment {
    private final static String TAG = "LotsFragmentTag";
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private ArrayList<Lot> lots = new ArrayList<>();
    private String key;


    public static LotsFragment newInstance(String key){
        LotsFragment lotsFragment = new LotsFragment();
        Bundle args = new Bundle();
        args.putString("key",key);
        lotsFragment.setArguments(args);
        return lotsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
        key = getArguments().getString("key");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView()");
        View view = inflater.inflate(R.layout.fragment_lots,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.lots_fragment_recyclerView);
        gridLayoutManager = new GridLayoutManager(getContext(),5);
        gridLayoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(new LotAdapter(getLots(key),key));
        return view;
    }

    private ArrayList<Lot> getLots(String key) {
        Log.d(TAG,"getLots()");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference lotsRef = rootRef.child(LOTS_NODE_PREFIX+key).getRef();
        lotsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lots.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    Long id = Long.valueOf(ds.getKey());
                    Long primaryStatus = (Long) ds.child(LOTS_PRIMARY_STATUS_PREFIX).getValue();
                    Long secondaryStatus = (Long) ds.child(LOTS_SECONDARY_STATUS_PREFIX).getValue();
                    lots.add(new Lot(id,primaryStatus,secondaryStatus));
                }
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return lots;
    }


}
