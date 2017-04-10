package com.heapdragon.lots;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Francesco on 2017-04-09.
 */

public class SignOutAppCompat extends AppCompatActivity {
    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this,LoginActivity.class));
    }
}
