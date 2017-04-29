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
import android.widget.TextView;
import java.util.ArrayList;

public class LotAdapter extends RecyclerView.Adapter<LotAdapter.LotDotHolder> {

    private static final String TAG = "LotAdapter";
    private ArrayList<Lot> lots;
    private String siteKey;


    LotAdapter(ArrayList<Lot> lots, String siteKey) {
        Log.d(TAG,"LotAdapter()");
        this.lots = lots;
        this.siteKey = siteKey;
    }

    static class LotDotHolder extends RecyclerView.ViewHolder {
        FloatingActionButton innerDot;
        TextView number;
        String siteKey;
        Lot lot;
        LotDotHolder(View itemView) {
            super(itemView);
            innerDot = (FloatingActionButton) itemView.findViewById(R.id.inside_bitch);
            number = (TextView) itemView.findViewById(R.id.lot_dot_number);
            innerDot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startLotActivity(LotDotHolder.this,(int)lot.getNumber());
                }
            });
        }

        private void startLotActivity(LotDotHolder holder,int lotNumber){
            Context context = holder.innerDot.getContext();
            Intent intent = new Intent(context,LotActivity.class);
            intent.putExtra("siteKey", siteKey);
            intent.putExtra("status",holder.lot.getPrimaryStatus());
            intent.putExtra("lotNumber",lotNumber);
            context.startActivity(intent);
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
        holder.lot = lots.get(position);
        holder.siteKey = siteKey;
        holder.number.setText(String.valueOf(holder.lot.getNumber()));
        holder.innerDot.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.innerDot.getContext(),R.color.colorRed)));
    }


    @Override
    public int getItemCount() {return lots.size();}

}
