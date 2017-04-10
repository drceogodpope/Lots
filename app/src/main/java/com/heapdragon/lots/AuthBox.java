package com.heapdragon.lots;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

class AuthBox {
    private static final String TAG = "AUTHBOX";
    private FirebaseAuth auth;
    private Context context;
    private FirebaseAuth.AuthStateListener authStateListener;

     AuthBox(final Context context){
        auth = FirebaseAuth.getInstance();
        this.context = context;
            authStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    Log.d(TAG,"onAuthStateChanged()");
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if(user != null){
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                        startMainActivity();
                    }
                    else {
                        Log.d(TAG, "onAuthStateChanged:signed_out");
                    }
                }
            };


    }

    void startSignIn(String username,String password){
        auth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(context, "Incorrect Email/Password!", Toast.LENGTH_SHORT).show();
                }
                else {
                    startMainActivity();
                }
            }
        });
    }

    private void startMainActivity(){
        context.startActivity(new Intent(context,MainActivity.class));
    }

    void setAuthStateListener(){
        auth.addAuthStateListener(authStateListener);
    }
    void unsetAuthStateListener(){
        if(auth!=null) auth.removeAuthStateListener(authStateListener);
    }
}
