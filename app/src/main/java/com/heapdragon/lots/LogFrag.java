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
import android.widget.LinearLayout;
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

import static com.heapdragon.lots.DataBaseConstants.LOG_FIELD_UPDATED;
import static com.heapdragon.lots.DataBaseConstants.LOG_NODE_PREFIX;
import static com.heapdragon.lots.DataBaseConstants.LOG_NUMBER;
import static com.heapdragon.lots.DataBaseConstants.LOG_STATUS;
import static com.heapdragon.lots.DataBaseConstants.LOG_TIME_STAMP;

public abstract class LogFrag extends Fragment {

    private static final String TAG = "LogFrag";
    private RecyclerView recyclerView;
    private String key;
    private LinearLayout noLogsLayout;
    private ArrayList<SiteLog> logs;

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
        noLogsLayout = (LinearLayout) view.findViewById(R.id.no_logs_layout);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new LogAdapter(getLogs(key)));
        return view;
    }

    protected ArrayList<SiteLog> getLogs(String key) {
        Log.d(TAG, "getLogs()");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference logRef = rootRef.child(LOG_NODE_PREFIX + key).getRef();
        logRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                logs.clear();
                traverseLogs(dataSnapshot);
                checkIfLogsExist();
                orderLogs();
                recyclerView.getAdapter().notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Something went wrong... Try again", Toast.LENGTH_SHORT).show();
            }
        });
        return logs;
    }

    protected void traverseLogs(DataSnapshot dataSnapshot){
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            if(checkField(ds)) addSiteLog(ds);
        }
    }

    // OVERRIDE ME TO FILTER PRIMARY/SECONDARY LOGS
    protected void addSiteLog(DataSnapshot ds){
        String logKey = ds.getKey();
        long lotNumber = (long) ds.child(LOG_NUMBER).getValue();
        DateTime dateTime = new DateTime(ds.child(LOG_TIME_STAMP).getValue());
        long status = (long) ds.child(LOG_STATUS).getValue();
        logs.add(new SiteLog(dateTime, lotNumber, (int) status,logKey,LogFrag.this.key));
    }

    abstract boolean checkField(DataSnapshot ds);

    private void checkIfLogsExist(){
        if(logs.size()==0){
            noLogsLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            noLogsLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void orderLogs(){
        Collections.sort(logs, new Comparator<SiteLog>() {
            @Override
            public int compare(SiteLog siteLog, SiteLog t1) {
                return t1.getDateTime().compareTo(siteLog.getDateTime());
            }
        });
    }







}

