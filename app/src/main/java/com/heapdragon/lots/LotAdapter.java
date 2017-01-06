package com.heapdragon.lots;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Francesco on 2017-01-04.
 */

public class LotAdapter extends RecyclerView.Adapter<LotAdapter.LotDotHolder> {

    private ArrayList<Lot> lots;

    public LotAdapter(ArrayList<Lot> lots) {
        this.lots = lots;
    }

    public class LotDotHolder extends RecyclerView.ViewHolder {
        protected FloatingActionButton dot;
        protected TextView lotNumber;
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
    public void onBindViewHolder(LotDotHolder holder, int position) {
        holder.dot.setBackgroundColor(ContextCompat.getColor(holder.dot.getContext(),R.color.colorCyan));
        holder.lotNumber.setText(String.valueOf(lots.get(position).getNumber()));
    }

    @Override
    public int getItemCount() {
        return lots.size();
    }

}
