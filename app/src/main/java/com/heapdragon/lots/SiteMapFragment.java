package com.heapdragon.lots;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.net.URI;

import static android.app.Activity.RESULT_OK;
import static com.heapdragon.lots.DataBaseConstants.SITE_MAPS_ROOT;

public class SiteMapFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "SiteMapFragment";
    private LinearLayout linearLayout;
    private ImageButton addSiteMapButton;
    private TouchImageView siteMapImageView;
    private FirebaseStorage mStorageRef;
    private ProgressBar progressBar;
    private final static int GALLERY_INTENT = 1;
    private byte[] compressedBitmap;

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
        siteMapImageView = (TouchImageView) view.findViewById(R.id.site_map_image_view);
        addSiteMapButton = (ImageButton) view.findViewById(R.id.add_site_map_button);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadSiteMapImage(getArguments().getString("key"));

        addSiteMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseSiteMap();
            }
        });

        siteMapImageView.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                startFullScreenActivity();
                return true;
            }
            @Override
            public boolean onDoubleTap(MotionEvent motionEvent) {
                return false;
            }
            @Override
            public boolean onDoubleTapEvent(MotionEvent motionEvent) {
                return false;
            }
        });
    }

    // CHOOSE SITE MAP METHODS //
    private void chooseSiteMap() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_INTENT);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==GALLERY_INTENT && resultCode == RESULT_OK){
            setSiteMap(data.getData());
            linearLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }
    private boolean setSiteMap(Uri data) {
        StorageReference filePath = mStorageRef.getReference().child(SITE_MAPS_ROOT).child(getArguments().getString("key"));
        try{
            filePath.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(),"Site map uploaded!",Toast.LENGTH_SHORT).show();
                    loadSiteMapImage(getArguments().getString("key"));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG,e.toString());
                    Toast.makeText(getContext(),"Error uploading site map!",Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    // CHOOSE SITE MAP METHODS //


    private void loadSiteMapImage(String key) {
        Toast.makeText(getContext(), "loadSiteMap()", Toast.LENGTH_SHORT).show();
        StorageReference siteMapRef = mStorageRef.getReference().child(SITE_MAPS_ROOT).child(key);
        siteMapRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Toast.makeText(getContext(), "glide success", Toast.LENGTH_SHORT).show();
                Glide.with(getContext()).load(uri).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                         Thread compressThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                compressedBitmap = Utility.compressBitmap(resource);
                            }
                        });
                        compressThread.start();
                        siteMapImageView.setImageBitmap(resource);
                    }
                });
                siteMapImageView.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                siteMapImageView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }
        });
    }


    private void startFullScreenActivity() {
        Intent intent = new Intent(getActivity(),FullScreenActivity.class);
        intent.putExtra("bitmap",compressedBitmap);
        startActivity(intent);
    }



}
