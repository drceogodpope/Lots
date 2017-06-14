package com.heapdragon.lots;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import static com.heapdragon.lots.DataBaseConstants.LOTS_NODE_PREFIX;
import static com.heapdragon.lots.DataBaseConstants.READY_LOTS_NODE;
import static com.heapdragon.lots.DataBaseConstants.SITES_NODE;

class SiteAdapter extends RecyclerView.Adapter<SiteAdapter.SiteCardViewHolder> {
    public static final String TAG = "SiteAdapter";
    private ArrayList<Site> sites;

    SiteAdapter(ArrayList<Site> sites) {
        Collections.shuffle(sites);
        this.sites = sites;
    }

    static class SiteCardViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageButton issueButton;
        TextView readyLots;
        TextView siteName;
        TextView totalLots;
        TextView mapButton;
        TextView lotsButton;
        int[] siteColors;
        protected Site site;

        public SiteCardViewHolder(View itemView) {
            super(itemView);
            siteColors = itemView.getContext().getResources().getIntArray(R.array.siteColors);
            cardView = (CardView) itemView.findViewById(R.id.site_card);
            issueButton = (ImageButton) itemView.findViewById(R.id.site_card_issue_image);
            readyLots = (TextView) itemView.findViewById(R.id.site_card_total_ready);
            siteName = (TextView) itemView.findViewById(R.id.site_card_name);
            totalLots = (TextView) itemView.findViewById(R.id.site_card_total_lots);
            mapButton = (TextView) itemView.findViewById(R.id.site_card_map_button);
            lotsButton = (TextView) itemView.findViewById(R.id.site_card_lot_button);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(cardView.getContext(),SiteActivity.class);
                    intent.putExtra("key",site.getId());
                    intent.putExtra("name",siteName.getText().toString());
                    intent.putExtra("color",((ColorDrawable)cardView.getBackground()).getColor());
                    cardView.getContext().startActivity(intent);
                }
            });

            mapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(cardView.getContext(),SiteActivity.class);
                    intent.putExtra("key",site.getId());
                    intent.putExtra("name",siteName.getText().toString());
                    intent.putExtra("color",((ColorDrawable)cardView.getBackground()).getColor());
                    intent.putExtra("adapterPage",1);
                    cardView.getContext().startActivity(intent);
                }
            });

            lotsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(cardView.getContext(),SiteActivity.class);
                    intent.putExtra("key",site.getId());
                    intent.putExtra("name",siteName.getText().toString());
                    intent.putExtra("color",((ColorDrawable)cardView.getBackground()).getColor());
                    intent.putExtra("adapterPage",2);
                    cardView.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public SiteCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.fragment_site_card,parent,false);
        return new SiteCardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SiteCardViewHolder holder, int position) {
        final Site site = sites.get(position);
        holder.site = site;
        Log.d(TAG,String.valueOf("CARD CORNER RADIUS: "+holder.cardView.getRadius()));
        holder.siteName.setText(site.getName());
        holder.totalLots.setText("Lots " +String.valueOf(site.getN())+" to " + String.valueOf(site.getM()));
        holder.cardView.setPreventCornerOverlap(false);
        holder.readyLots.setText(String.valueOf(site.getReadyLots()));
        if(site.getIssue_lots()<1){
            holder.issueButton.setVisibility(View.INVISIBLE);
        }
        else{
            holder.issueButton.setVisibility(View.VISIBLE);
        }
        android.util.Log.d(TAG,site.getName() + site.getSiteColor());


        try {
            holder.cardView.setBackgroundColor(holder.siteColors[holder.site.getSiteColor()]);
        }
        catch (Exception e){
            e.printStackTrace();
            holder.cardView.setBackgroundColor(holder.siteColors[0]);
        }


        //SETUP FOR COMPLETE LOT LISTENERS//
        setReadyLots(holder,site);
    }

    private void setReadyLots(SiteCardViewHolder holder1,Site site1){
        final SiteCardViewHolder holder = holder1;
        final Site site = site1;
        DatabaseReference lotRef = FirebaseDatabase.getInstance().getReference().child(LOTS_NODE_PREFIX+holder.site.getId());
        lotRef.orderByValue().equalTo(Lot.READY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final long count = dataSnapshot.getChildrenCount();
                final DatabaseReference sitesRef = FirebaseDatabase.getInstance().getReference().child(SITES_NODE);
                sitesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(site.getId()) && count>=0){
                            sitesRef.child(holder.site.getId()).child(READY_LOTS_NODE).setValue(count);
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
