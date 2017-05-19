package com.heapdragon.lots;

import java.util.ArrayList;

class SecondaryLogAdapter extends LogAdapter {

    SecondaryLogAdapter(ArrayList<SiteLog> logs) {
        super(logs);
    }

    @Override
    public void onBindViewHolder(LogCardHolder holder, int position) {
        holder.siteLog = logs.get(position);
        switch (holder.siteLog.getStatus()) {
            case Lot.MATERIAL_ORDERED:
                holder.outerDot.setColor(R.color.colorPink);
                break;
            case Lot.ARCH_IN_SHIPPING:
                holder.outerDot.setColor(R.color.colorPurple1);
                break;
            case Lot.ARCH_IN_PRODUCTION:
                holder.outerDot.setColor(R.color.colorOrange);
                break;
            default:
                holder.outerDot.setColor(R.color.colorGold2);
        }
        holder.innerDot.setColor(R.color.colorDarkBackground);
        holder.logNumber.setText(String.valueOf(holder.siteLog.getLotNumber()));
        holder.logTitle.setText(holder.siteLog.getTitle());
        holder.logUser.setText(holder.siteLog.getUser());
        holder.logTimeStamp.setText(String.valueOf(holder.siteLog.getTimeStamp()));
    }
}
