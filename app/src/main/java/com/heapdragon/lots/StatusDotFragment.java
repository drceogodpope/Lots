package com.heapdragon.lots;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
public abstract class  StatusDotFragment extends Fragment {

    protected String siteKey;
    protected int lotNumber;
    protected TextView statusText;
    protected TextView number;
    protected LotDot innerDot;
    protected LotDot outterDot;
    protected DatabaseReference dbRef;
    protected CardView cardView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        siteKey = getArguments().getString("siteKey");
        lotNumber = getArguments().getInt("lotNumber");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status_dot,container,false);
        number = (TextView)view.findViewById(R.id.status_dot_number);
        innerDot = (LotDot) view.findViewById(R.id.inner_dot);
        outterDot = (LotDot) view.findViewById(R.id.outter_dot);
        statusText = (TextView) view.findViewById(R.id.status_dot_text);
        cardView = (CardView) view.findViewById(R.id.status_dot_cardview);
        setDatabaseReference();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    protected abstract void setDatabaseReference();

    public abstract void setStatusText(Object value);

}
