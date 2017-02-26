package com.heapdragon.lots;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorSquareViewHolder> {

    private static final String TAG = "ColorAdapter";
    private int[] colors;

    ColorAdapter(int[] colors){
        this.colors = colors;
    }

    @Override
    public ColorSquareViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.site_color_chooser_square,parent,false);
        Log.d(TAG,"COLORS IS SIZE " + String.valueOf(colors.length));
        return new ColorAdapter.ColorSquareViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ColorSquareViewHolder holder, final int position) {
        holder.background.setBackgroundColor(colors[position]);
        holder.background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(holder.background.getContext(), String.valueOf(holder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }
        });
        Log.d(TAG,"binding + " + String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return colors.length;
    }

     class ColorSquareViewHolder extends RecyclerView.ViewHolder {
        protected RelativeLayout background;
         ColorSquareViewHolder(View itemView) {
            super(itemView);
            background = (RelativeLayout) itemView.findViewById(R.id.colorSquare);
        }
    }

}
