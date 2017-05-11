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
        LotDot outterdot;
        LotDot innerDot;
        TextView number;
        String siteKey;
        Lot lot;
        LotDotHolder(View itemView) {
            super(itemView);
            innerDot = (LotDot) itemView.findViewById(R.id.inner_shit);
            outterdot = (LotDot) itemView.findViewById(R.id.outter_shit);

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

        switch ((int)holder.lot.getPrimaryStatus()){
            case Lot.NOT_READY:
                holder.innerDot.setColor(R.color.colorRed);
                break;
            case Lot.READY:
                holder.innerDot.setColor(R.color.colorGreen);
                break;
            case Lot.ISSUE:
                holder.innerDot.setColor(R.color.colorYellow);
                break;
            default:
                holder.innerDot.setColor(R.color.colorGrey);

        }

        switch ((int)holder.lot.getSecondaryStatus()){
            case Lot.MATERIAL_ORDERED:
                holder.outterdot.setColor(R.color.colorPink);
                break;
            case Lot.ARCH_IN_PRODUCTION:
                holder.outterdot.setColor(R.color.colorOrange);
                break;
            case Lot.ARCH_IN_SHIPPING:
                holder.outterdot.setColor(R.color.colorPurple1);
                break;
            case Lot.ARCH_REQUIRED:
                holder.outterdot.setColor(R.color.colorGold2);
                break;
            default:
                holder.outterdot.setBackgroundTintList(holder.innerDot.getColorStateList());

        }


    }


    @Override
    public int getItemCount() {return lots.size();}

}
