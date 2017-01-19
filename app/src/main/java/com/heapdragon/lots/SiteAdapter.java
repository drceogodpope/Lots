package com.heapdragon.lots;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.heapdragon.lots.DataBaseConstants.LOTS_NODE_PREFIX;
import static com.heapdragon.lots.DataBaseConstants.READY_LOTS_NODE;
import static com.heapdragon.lots.DataBaseConstants.SITES_NODE;

public class SiteAdapter extends RecyclerView.Adapter<SiteAdapter.SiteCardViewHolder> {
    public static final String TAG = "SiteAdapter";
    protected ArrayList<Site> sites;
    protected SiteAdapter(ArrayList<Site> sites) {
        this.sites = sites;
    }

    public class SiteCardViewHolder extends RecyclerView.ViewHolder {
        protected CardView cardView;
        protected ImageButton issueButton;
        protected TextView readyLots;
        protected TextView siteName;
        protected TextView totalLots;
        protected TextView mapButton;
        protected TextView lotsButton;
        protected Site site;

        public SiteCardViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.site_card);
            issueButton = (ImageButton) itemView.findViewById(R.id.site_card_issue_image);
            readyLots = (TextView) itemView.findViewById(R.id.site_card_total_ready);
            siteName = (TextView) itemView.findViewById(R.id.site_card_name);
            totalLots = (TextView) itemView.findViewById(R.id.site_card_total_lots);
            mapButton = (TextView) itemView.findViewById(R.id.site_card_map_button);
            lotsButton = (TextView) itemView.findViewById(R.id.site_card_lot_button);
        }
    }

    @Override
    public SiteCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_site_card,parent,false);
        return new SiteCardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SiteCardViewHolder holder, int position) {
        final Site site = sites.get(position);
        holder.site = site;
        holder.cardView.setRadius(10);
        holder.siteName.setText(site.getName());
        holder.totalLots.setText(String.valueOf(site.getNumberOfLots()));
        holder.readyLots.setText(String.valueOf(site.getReadyLots()));
        if(site.getIssue_lots()<1){
            holder.issueButton.setVisibility(View.INVISIBLE);
        }
        else{
            holder.issueButton.setVisibility(View.VISIBLE);
        }
        android.util.Log.d(TAG,site.getName() + site.getSiteColor());
        switch (site.getSiteColor()){
            case 0: holder.cardView.setBackgroundColor(ContextCompat.getColor(holder.cardView.getContext(),R.color.colorCyan));
                break;
            case 1: holder.cardView.setBackgroundColor(ContextCompat.getColor(holder.cardView.getContext(),R.color.colorPink));
                break;
            case 2: holder.cardView.setBackgroundColor(ContextCompat.getColor(holder.cardView.getContext(),R.color.colorPurple));
                break;
            case 3: holder.cardView.setBackgroundColor(ContextCompat.getColor(holder.cardView.getContext(),R.color.colorIndigo));
                break;

        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.cardView.getContext(),SiteActivity.class);
                intent.putExtra("key",site.getId());
                intent.putExtra("name",holder.siteName.getText().toString());
                intent.putExtra("color",((ColorDrawable)holder.cardView.getBackground()).getColor());
                holder.cardView.getContext().startActivity(intent);
            }
        });

        DatabaseReference lotRef = FirebaseDatabase.getInstance().getReference().child(LOTS_NODE_PREFIX+holder.site.getId());
        lotRef.orderByValue().equalTo(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    count++;
                }
                final DatabaseReference sitesRef = FirebaseDatabase.getInstance().getReference().child(SITES_NODE);
                final int finalCount = count;
                sitesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(site.getId()).exists()){
                            sitesRef.child(holder.site.getId()).child(READY_LOTS_NODE).setValue(finalCount);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                holder.readyLots.setText(String.valueOf(count));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return sites.size();
    }
}
