package com.heapdragon.lots;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Francesco on 2016-12-26.
 */

public class SiteAdapter extends RecyclerView.Adapter<SiteAdapter.SiteCardViewHolder> {

    ArrayList<Site> sites;

    public SiteAdapter(ArrayList<Site> sites) {
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
    public void onBindViewHolder(SiteCardViewHolder holder, int position) {
        Site site = sites.get(position);
        holder.siteName.setText(site.getName());
        holder.totalLots.setText(String.valueOf(site.getTotalLots()));
        holder.readyLots.setText(String.valueOf(site.getReadyLots()));
        if(site.getIssue_lots()<1){
            holder.issueButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return sites.size();
    }
}
