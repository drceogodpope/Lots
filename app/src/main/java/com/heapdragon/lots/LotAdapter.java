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


    public LotAdapter(ArrayList<Lot> lots,String siteKey) {
        Log.d(TAG,"LotAdapter()");
        this.lots = lots;
        this.siteKey = siteKey;
    }

    public class LotDotHolder extends RecyclerView.ViewHolder {
        FloatingActionButton innerDot;
        FloatingActionButton outterDot;
        TextView lotNumber;
        Lot lot;
        LotDotHolder(View itemView) {
            super(itemView);
            innerDot = (FloatingActionButton) itemView.findViewById(R.id.inner_dot);
            outterDot = (FloatingActionButton) itemView.findViewById(R.id.outter_dot);
            lotNumber = (TextView) itemView.findViewById(R.id.lot_dot_number);
            innerDot.setSize(FloatingActionButton.SIZE_MINI);
            outterDot.setSize(FloatingActionButton.SIZE_NORMAL);
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
        switch ((int)holder.lot.getPrimaryStatus()){
            case 0:
                Log.d(TAG,"SET DOT COLOR TO RED");
                holder.innerDot.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.innerDot.getContext(),R.color.colorRed)));
                break;
            case 1:
                holder.innerDot.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.innerDot.getContext(),R.color.colorGreen)));
                break;
            case 2:
                holder.innerDot.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.innerDot.getContext(),R.color.colorYellow)));
                break;
            default:
                holder.innerDot.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.innerDot.getContext(),R.color.colorGrey)));
        }

        switch ((int)holder.lot.getSecondaryStatus()){
            case 0:
                Log.d(TAG,"SET DOT COLOR TO RED");
                holder.outterDot.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.innerDot.getContext(),R.color.colorRed)));
                break;
            case 2:
                holder.outterDot.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.innerDot.getContext(),R.color.colorGreen)));
                break;
            case 1:
                holder.outterDot.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.innerDot.getContext(),R.color.colorYellow)));
                break;
            default:
                holder.outterDot.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.innerDot.getContext(),R.color.colorGrey)));
        }


        holder.lotNumber.setText(String.valueOf(lotNumber));
        holder.innerDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = holder.innerDot.getContext();
                Intent intent = new Intent(context,LotActivity.class);
                intent.putExtra("siteKey", siteKey);
                intent.putExtra("status",holder.lot.getPrimaryStatus());
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
