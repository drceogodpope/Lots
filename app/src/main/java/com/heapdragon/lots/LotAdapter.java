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
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class LotAdapter extends RecyclerView.Adapter<LotAdapter.LotDotHolder> {

    private static final String TAG = "LotAdapter";
    private ArrayList<Lot> lots;
    private String key;


    public LotAdapter(ArrayList<Lot> lots,String key) {
        Log.d(TAG,"LotAdapter()");
        this.lots = lots;
        this.key = key;
    }

    public class LotDotHolder extends RecyclerView.ViewHolder {
        protected FloatingActionButton dot;
        protected TextView lotNumber;
        protected Lot lot;
        public LotDotHolder(View itemView) {
            super(itemView);
            dot = (FloatingActionButton) itemView.findViewById(R.id.lot_dot_dot);
            lotNumber = (TextView) itemView.findViewById(R.id.lot_dot_number);
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
        holder.lot = lots.get(position);
        switch ((int)holder.lot.getStatus()){
            case 0:
                Log.d(TAG,"SET DOT COLOR TO RED");
                holder.dot.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.dot.getContext(),R.color.colorRed)));
                break;
            case 1:
                holder.dot.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.dot.getContext(),R.color.colorGreen)));
                break;
            case 2:
                holder.dot.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.dot.getContext(),R.color.colorYellow)));
                break;
            default:
                holder.dot.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.dot.getContext(),R.color.colorGrey)));
        }
        holder.lotNumber.setText(String.valueOf(lotNumber));
        holder.dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = holder.dot.getContext();
                Intent intent = new Intent(context,LotActivity.class);
                intent.putExtra("key",key);
                intent.putExtra("status",holder.lot.getStatus());
                intent.putExtra("lotNumber",lotNumber);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return lots.size();
    }

}
