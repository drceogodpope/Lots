package com.heapdragon.lots;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class LotAdapter extends RecyclerView.Adapter<LotAdapter.LotDotHolder> {

    private static final String TAG = "LotAdapter";
    private ArrayList<Lot> lots;
    private String siteKey;


    public LotAdapter(ArrayList<Lot> lots,String siteKey) {
        Log.d(TAG,"LotAdapter()");
        this.lots = lots;
        this.siteKey = siteKey;
    }

    public class LotDotHolder extends RecyclerView.ViewHolder {
        LotDot lotDot;
        Lot lot;
        RelativeLayout root;
        LotDotHolder(View itemView) {
            super(itemView);
            root = (RelativeLayout) itemView.findViewById(R.id.lot_fragment_root);
            lotDot = new LotDot(itemView.getContext());
        }
    }

    @Override
    public LotDotHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lot_dot,parent,false);
        return new LotDotHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final LotDotHolder holder, int position) {
        Log.d(TAG,"onBindViewHolder()");
        final int lotNumber = (int) lots.get(position).getNumber();
        holder.root.addView(holder.lotDot);
        holder.lotDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLotActivity(holder,lotNumber);
            }
        });
    }

    private void startLotActivity(LotDotHolder holder,int lotNumber){
        Context context = holder.lotDot.getContext();
                Intent intent = new Intent(context,LotActivity.class);
                intent.putExtra("siteKey", siteKey);
                intent.putExtra("status",holder.lot.getPrimaryStatus());
                intent.putExtra("lotNumber",lotNumber);
                context.startActivity(intent);
    }

    @Override
    public int getItemCount() {return lots.size();}

}
