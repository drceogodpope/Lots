package com.heapdragon.lots;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogCardHolder> {
    private static final String TAG = "LogAdapter";

    protected ArrayList<SiteLog> logs;

    LogAdapter(ArrayList<SiteLog> logs){
        this.logs = logs;
   }

    protected class LogCardHolder extends RecyclerView.ViewHolder {
        LotDot innerDot;
        LotDot outerDot;
        View lotDotView;
        TextView logNumber;
        TextView logTitle;
        TextView logUser;
        TextView logTimeStamp;
        SiteLog siteLog;
        CardView logCard;

        LogCardHolder(View itemView) {
            super(itemView);
            lotDotView = (itemView.findViewById(R.id.lot_dot_view));
            innerDot = (LotDot) lotDotView.findViewById(R.id.inner_shit);
            outerDot = (LotDot) lotDotView.findViewById(R.id.outter_shit);
            logNumber = (TextView) lotDotView.findViewById(R.id.lot_dot_number);
            logTitle = (TextView) itemView.findViewById(R.id.log_title);
            logUser = (TextView) itemView.findViewById(R.id.log_user);
            logTimeStamp = (TextView) itemView.findViewById(R.id.log_time_stamp);
            logUser.setVisibility(View.GONE);
            logCard = (CardView) itemView.findViewById(R.id.log_card);

            logCard.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    deleteSite(siteLog.getSiteKey(),siteLog.getLogKey());
                    return false;
                }
            });
        }
        private void deleteSite(String siteKey,String logKey){
            DatabaseReference logRef = FirebaseDatabase.getInstance().getReference().
                    child(DataBaseConstants.LOG_NODE_PREFIX+siteKey).child(logKey);
            logRef.removeValue();
        }
    }

    @Override
    public LogCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_card_e,parent,false);
        return new LogCardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final LogCardHolder holder, int position) {
        holder.siteLog = logs.get(position);
        holder.innerDot.setVisibility(View.INVISIBLE);
        holder.logNumber.setText(String.valueOf(holder.siteLog.getLotNumber()));
        holder.logTitle.setText(holder.siteLog.getTitle());
        holder.logUser.setText(holder.siteLog.getUser());
        holder.logTimeStamp.setText(String.valueOf(holder.siteLog.getTimeStamp()));
        switch (holder.siteLog.getStatus()){
            case 0:
                holder.outerDot.setColor(R.color.colorRed);
                break;
            case 1:
                holder.outerDot.setColor(R.color.colorGreen);
                break;
            case 2:
                holder.outerDot.setColor(R.color.colorYellow);
                break;
            default:
                holder.outerDot.setColor(R.color.colorGrey);
        }
        holder.logCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Log.d(TAG,String.valueOf(holder.getAdapterPosition()));
                if(holder.getAdapterPosition()>-1){
                    logs.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                    DatabaseReference logRef = FirebaseDatabase.getInstance().getReference().child(DataBaseConstants.LOG_NODE_PREFIX+holder.siteLog.getSiteKey()).child(holder.siteLog.getLogKey());
                    logRef.removeValue();
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return logs.size();
    }

}
