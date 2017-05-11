package com.heapdragon.lots;

import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogCardHolder> {

    private ArrayList<SiteLog> logs;

    LogAdapter(ArrayList<SiteLog> logs){
        this.logs = logs;
   }

    static class LogCardHolder extends RecyclerView.ViewHolder {
        protected FloatingActionButton logDot;
        protected TextView logNumber;
        protected TextView logTitle;
        protected TextView logUser;
        protected TextView logTimeStamp;
        protected SiteLog siteLog;
        protected CardView logCard;

        LogCardHolder(View itemView) {
            super(itemView);
            logDot = (FloatingActionButton) itemView.findViewById(R.id.log_dot);
            logNumber = (TextView) itemView.findViewById(R.id.log_number);
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_card,parent,false);
        return new LogCardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LogCardHolder holder, int position) {
        holder.siteLog = logs.get(position);
        switch (holder.siteLog.getStatus()){
            case 0:
                holder.logDot.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.logDot.getContext(),R.color.colorRed)));
                break;
            case 1:
                holder.logDot.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.logDot.getContext(),R.color.colorGreen)));
                break;
            case 2:
                holder.logDot.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.logDot.getContext(),R.color.colorYellow)));
                break;
            default:
                holder.logDot.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.logDot.getContext(),R.color.colorGrey)));
        }
        holder.logNumber.setText(String.valueOf(holder.siteLog.getLotNumber()));
        holder.logTitle.setText(holder.siteLog.getTitle());
        holder.logUser.setText(holder.siteLog.getUser());
        holder.logTimeStamp.setText(String.valueOf(holder.siteLog.getTimeStamp()));

    }

    @Override
    public int getItemCount() {
        return logs.size();
    }


}
