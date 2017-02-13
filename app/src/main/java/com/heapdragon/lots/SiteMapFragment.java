package com.heapdragon.lots;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.net.URI;

import static com.heapdragon.lots.DataBaseConstants.SITE_MAPS_ROOT;

public class SiteMapFragment extends android.support.v4.app.Fragment {
    private LinearLayout linearLayout;
    private ImageButton addSiteMapButton;
    private ImageView siteMapImageView;
    private FirebaseStorage mStorageRef;
    private ProgressBar progressBar;

    public static SiteMapFragment newInstance(String key){
        SiteMapFragment siteMapFragment = new SiteMapFragment();
        Bundle args = new Bundle();
        args.putString("key",key);
        siteMapFragment.setArguments(args);
        return siteMapFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStorageRef = FirebaseStorage.getInstance();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map,container,false);
        linearLayout = (LinearLayout) view.findViewById(R.id.add_site_map_layout);
        progressBar = (ProgressBar) view.findViewById(R.id.siteMap_progressBar);
        siteMapImageView = (ImageView) view.findViewById(R.id.site_map_image_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadSiteMapImage(getArguments().getString("key"));
    }

    private void loadSiteMapImage(String key){
        StorageReference siteMapRef = mStorageRef.getReference().child(SITE_MAPS_ROOT).child(key);

        siteMapRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getContext()).load(uri.toString()).into(siteMapImageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }
        });

    }

}
