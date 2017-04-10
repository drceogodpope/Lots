package com.heapdragon.lots;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity{

    private AuthBox authBox;
    private TextView usernameField;
    private EditText passwordField;
    private Button loginButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        authBox = new AuthBox(this);
        usernameField = (EditText) findViewById(R.id.username_field);
        passwordField = (EditText) findViewById(R.id.password_field);
        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();
                if(!(TextUtils.isEmpty(username)||TextUtils.isEmpty(password))){
                    authBox.startSignIn(username,password);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Check Fields!",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        authBox.setAuthStateListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        authBox.unsetAuthStateListener();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Splash.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }
}
