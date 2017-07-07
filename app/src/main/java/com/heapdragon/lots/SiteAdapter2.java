package com.heapdragon.lots;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
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
import java.util.Collections;
import static com.heapdragon.lots.DataBaseConstants.LOTS_NODE_PREFIX;
import static com.heapdragon.lots.DataBaseConstants.READY_LOTS_NODE;
import static com.heapdragon.lots.DataBaseConstants.SITES_NODE;

class SiteAdapter2 extends RecyclerView.Adapter<SiteAdapter2.SiteCardViewHolder2> {
    public static final String TAG = "SiteAdapter";
    private ArrayList<Site> sites;

    SiteAdapter2(ArrayList<Site> sites) {
        this.sites = sites;
    }

    @Override
    public SiteCardViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.fragment_site_card,parent,false);
        return new SiteCardViewHolder2(itemView);
    }

    static class SiteCardViewHolder2 extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView siteName;
        TextView totalLots;
        int[] siteColors;
        protected Site site;

        SiteCardViewHolder2(View itemView) {
            super(itemView);
            siteColors = itemView.getContext().getResources().getIntArray(R.array.siteColors);
            cardView = (CardView) itemView.findViewById(R.id.site_card);
            totalLots = (TextView) itemView.findViewById(R.id.site_card_total_lots);
            siteName = (TextView) itemView.findViewById(R.id.site_card_name);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(cardView.getContext(),SiteActivity.class);
                    intent.putExtra("key",site.getId());
                    intent.putExtra("name",siteName.getText().toString());
                    intent.putExtra("color",(site.getSiteColor()));
                    cardView.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(final SiteCardViewHolder2 holder, int position) {
        final Site site = sites.get(position);
        holder.site = site;
        holder.siteName.setText(site.getName());
        holder.totalLots.setText(holder.site.lotIntervalsToString());
        try {
            holder.cardView.setCardBackgroundColor(holder.siteColors[holder.site.getSiteColor()]);
        }
        catch (Exception e) {
            e.printStackTrace();
            holder.cardView.setCardBackgroundColor(holder.siteColors[0]);
        }
        holder.cardView.setUseCompatPadding(true);
    }
    @Override
    public int getItemCount() {
        return sites.size();
    }
}
