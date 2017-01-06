package com.heapdragon.lots;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import static com.heapdragon.lots.DataBaseConstants.LOTS_NODE_PREFIX;
import static com.heapdragon.lots.DataBaseConstants.SITES_NODE;
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
        Toast.makeText(getContext(),"onCreateView()",Toast.LENGTH_SHORT).show();
        View view = inflater.inflate(R.layout.fragment_lots,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.lots_fragment_recyclerView);
        gridLayoutManager = new GridLayoutManager(getContext(),5);
        gridLayoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(new LotAdapter(getLot(key)));
        return view;
    }

    private ArrayList<Lot> getLot(String key) {
        Log.d(TAG,"getLot()");
        lots.clear();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference lotsRef = rootRef.child(LOTS_NODE_PREFIX+key).getRef();
        lotsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    Long id = Long.valueOf(ds.getKey());
                    Long status = (Long) ds.getValue();
                    lots.add(new Lot(id,status));
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
