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
import android.widget.TextView;
import android.widget.Toast;
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

public class SiteLogFragment extends Fragment {

    private final String TAG = "SiteLogFragment";

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private String key;
    private TextView textView;
    protected int lotNumber = 0;
    private ArrayList<SiteLog> logs;

    public static SiteLogFragment newInstance(String key) {
        Bundle args = new Bundle();
        args.putString("key", key);
        SiteLogFragment fragment = new SiteLogFragment();
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
        View view = inflater.inflate(R.layout.fragment_log, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.log_fragment_recyclerView);
        textView = (TextView) view.findViewById(R.id.log_fragment_textview);
        linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new LogAdapter(getLogs(key, lotNumber)));
        return view;
    }

    private ArrayList<SiteLog> getLogs(String key, final int lotID) {
        Log.d(TAG, "getLogs()");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference logRef = rootRef.child(LOG_NODE_PREFIX + key).getRef();
        logRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                logs.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.d(TAG, String.valueOf(ds.child(LOG_NUMBER).getValue()));
                    try {
                        long lotNumber = (long) ds.child(LOG_NUMBER).getValue();
                        Log.d(TAG, "Fragment's Lot Number = " + String.valueOf(lotID) +
                                "Lot Number retrieved = " + String.valueOf(lotNumber));
                        if (lotID == 0 || (int) lotNumber == lotID) {
                            DateTime dateTime = new DateTime(ds.child(LOG_TIME_STAMP).getValue());
                            long status = (long) ds.child(LOG_STATUS).getValue();
                            SiteLog siteLog = new SiteLog(dateTime, lotNumber, (int) status);
                            logs.add(siteLog);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                Collections.sort(logs, new Comparator<SiteLog>() {
                    @Override
                    public int compare(SiteLog siteLog, SiteLog t1) {
                        return t1.getDateTime().compareTo(siteLog.getDateTime());
                    }
                });
                if(logs.size()==0){
                    textView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }else{
                    textView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Something went wrong... Try again", Toast.LENGTH_SHORT).show();
            }
        });
        return logs;
    }

}

