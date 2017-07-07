package com.heapdragon.lots;

import android.content.Context;
import android.content.Intent;
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
        LotDot outerDot;
        LotDot innerDot;
        TextView number;
        String siteKey;
        Lot lot;
        LotDotHolder(View itemView) {
            super(itemView);
            innerDot = (LotDot) itemView.findViewById(R.id.inner_shit);
            outerDot = (LotDot) itemView.findViewById(R.id.outter_shit);

            number = (TextView) itemView.findViewById(R.id.lot_dot_number);
            innerDot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startEnhancedLotActivity(LotDotHolder.this,(int)lot.getNumber());
                }
            });

        }
        private void startEnhancedLotActivity(LotDotHolder holder,int lotNumber){
            Context context = holder.innerDot.getContext();
            Intent intent = new Intent(context,EnhancedLotActivity.class);
            intent.putExtra("siteKey", siteKey);
            intent.putExtra("lotNumber",lotNumber);
            context.startActivity(intent);
        }

    }

    @Override
    public LotDotHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lot_dot,parent,false);
        return new LotDotHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final LotDotHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder()");
        holder.lot = lots.get(position);
        holder.siteKey = siteKey;
        holder.number.setText(String.valueOf(holder.lot.getNumber()));


        if(holder.lot.getMaterialOrdered()){
            holder.innerDot.setColor(R.color.colorReady);
        }
        else{
            holder.innerDot.setColor(R.color.colorNotReady);
        }

        if(holder.lot.getArchLot()){
            switch ((int)holder.lot.getArchStatus()){
                case (int)Lot.WORK_ORDER_REQUIRED:
                    holder.outerDot.setColor(R.color.colorMaterialOrdered);
                    break;
                case (int)Lot.ARCH_IN_PRODUCTION_1:
                    holder.outerDot.setColor(R.color.colorArchInProduction);
                    break;
                case (int)Lot.ARCH_IN_SHIPPING_1:
                    holder.outerDot.setColor(R.color.colorArchInShipping);
                    break;
            }
        }
        else{
            holder.outerDot.setBackgroundTintList(holder.innerDot.getBackgroundTintList());
        }
    }


    @Override
    public int getItemCount() {return lots.size();}

}
