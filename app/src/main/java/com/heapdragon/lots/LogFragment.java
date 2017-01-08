package com.heapdragon.lots;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.heapdragon.lots.DataBaseConstants.LOG_NODE_PREFIX;
import static com.heapdragon.lots.DataBaseConstants.LOG_NUMBER;
import static com.heapdragon.lots.DataBaseConstants.LOG_STATUS;
import static com.heapdragon.lots.DataBaseConstants.LOG_TIME_STAMP;
import static com.heapdragon.lots.DataBaseConstants.LOTS_NODE_PREFIX;

public class LogFragment extends Fragment {
    private final String TAG = "LogFragment";

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private String key;
    private ArrayList<SiteLog> logs;

     public static LogFragment newInstance(String key) {
        Bundle args = new Bundle();
        args.putString("key",key);
        LogFragment fragment = new LogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        key = getArguments().getString("key");
        logs = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.log_fragment_recyclerView);
        linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new LogAdapter(getLogs(key)));
        return view;
    }

        private ArrayList<SiteLog> getLogs(String key) {
            Log.d(TAG,"getLogs()");
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference logRef = rootRef.child(LOG_NODE_PREFIX+key).getRef();
            logRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    logs.clear();
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        Log.d(TAG,String.valueOf(ds.child(LOG_NUMBER).getValue()));
                        try {
                            DateTime dateTime = new DateTime(ds.child(LOG_TIME_STAMP).getValue());
                            long status = (long) ds.child(LOG_STATUS).getValue();
                            long lotNumber = (long) ds.child(LOG_NUMBER).getValue();
                            SiteLog siteLog = new SiteLog(dateTime,lotNumber, (int) status);
                            logs.add(siteLog);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                    Collections.sort(logs, new Comparator<SiteLog>() {
                        @Override
                        public int compare(SiteLog siteLog, SiteLog t1) {
                            return t1.getDateTime().compareTo(siteLog.getDateTime());
                        }
                    });

                    recyclerView.getAdapter().notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getContext(),"Something went wrong... Try again",Toast.LENGTH_SHORT).show();
                }
            });
            return logs;
        }

}
