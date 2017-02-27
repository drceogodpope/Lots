package com.heapdragon.lots;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ColorChooserFrag extends Fragment {

    private RecyclerView colorRecyclerView;
    private GridLayoutManager gridLayoutManager;

    public static ColorChooserFrag newInstance() {
        return new ColorChooserFrag();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        int[] siteColors = getContext().getResources().getIntArray(R.array.siteColors);
        View view = inflater.inflate(R.layout.fragment_site_color_chooser,container,false);
        colorRecyclerView = (RecyclerView) view.findViewById(R.id.site_color_chooser_recyclerView);
        ColorAdapter adapter = new ColorAdapter(siteColors);
        colorRecyclerView.setAdapter(adapter);
        gridLayoutManager = new GridLayoutManager(getContext(),5);
        colorRecyclerView.setLayoutManager(gridLayoutManager);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}
