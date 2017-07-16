package com.heapdragon.lots;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.firebase.database.DatabaseReference;

public class LotSecondaryStatusFragment extends LotStatusFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.statusLevel = SiteLog.SECONDARY;
    }

    @Override
    protected void changeLotDot(int value1) {
        innerDot.setColor(R.color.colorDarkBackground);
        switch (value1){
            case Lot.MATERIAL_ORDERED:
                outerDot.setColor(R.color.colorMaterialOrdered);
                status.setText("Material Ordered");
                break;
            case Lot.ARCH_IN_SHIPPING:
                outerDot.setColor(R.color.colorArchInShipping);
                status.setText("Arch in Shipping");
                break;
            case Lot.ARCH_IN_PRODUCTION:
                outerDot.setColor(R.color.colorArchInProduction);
                status.setText("Arch in Production");
                break;
            default:
                outerDot.setColor(R.color.colorNotReady);
                status.setText("Arch " + "\n" + " Required");
        }
    }

    @Override
    protected void setNodeKey() {
        this.nodeKey = DataBaseConstants.LOTS_ARCH_LOT;
    }

    @Override
    protected void setButtons(final DatabaseReference statusRef) {
        b0.setBackgroundColor(getResources().getColor(R.color.colorMaterialOrdered));
        b1.setBackgroundColor(getResources().getColor(R.color.colorArchInShipping));
        b2.setBackgroundColor(getResources().getColor(R.color.colorArchInProduction));
        b3.setBackgroundColor(getResources().getColor(R.color.colorArchRequired));
        b0.setText("Material Ordered");
        b1.setText("Arch in Shipping");
        b2.setText("Arch in Production");
        b3.setText("Arch Required");


        b0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStatus(Lot.MATERIAL_ORDERED);
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStatus(Lot.ARCH_IN_SHIPPING);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStatus(Lot.ARCH_IN_PRODUCTION);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStatus(Lot.ARCH_REQUIRED);
            }
        });
    }
}
