package com.heapdragon.lots;

import android.content.Context;
import android.content.Intent;
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


    public LotAdapter(ArrayList<Lot> lots,String siteKey) {
        Log.d(TAG,"LotAdapter()");
        this.lots = lots;
        this.siteKey = siteKey;
    }

    public class LotDotHolder extends RecyclerView.ViewHolder {
        private ResizableFAB innerDot;
        private ResizableFAB outterDot;
        private TextView number;
        private Lot lot;
        LotDotHolder(View itemView) {
            super(itemView);
            innerDot = (ResizableFAB) itemView.findViewById(R.id.inside_bitch);
            outterDot = (ResizableFAB) itemView.findViewById(R.id.outter_dot);
            number = (TextView) itemView.findViewById(R.id.lot_dot_number);

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
        final int lotNumber = (int) lots.get(holder.getAdapterPosition()).getNumber();
        holder.innerDot.setDotColor(R.color.colorPink);
        holder.number.setText(String.valueOf(lotNumber));

        holder.innerDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLotActivity(holder,lotNumber);
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

    @Override
    public int getItemCount() {return lots.size();}

}
