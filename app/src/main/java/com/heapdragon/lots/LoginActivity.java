package com.heapdragon.lots;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private TextView tv;
    private EditText et;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tv = (TextView) findViewById(R.id.login_textView);
        et = (EditText) findViewById(R.id.login_activity_editText);
        setEnterID();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    View.OnKeyListener checkIdListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                switch (i) {
                    case KeyEvent.KEYCODE_ENTER:
                        if (checkId()) {
                            setEnterPassword();
                            return true;
                        } else {
                            Toast.makeText(LoginActivity.this, "Incorrect identification!", Toast.LENGTH_SHORT).show();
                            setEnterID();
                        }
                        return true;
                    default:
                        break;
                }
            }
            return false;
        }
    };

    View.OnKeyListener checkPasswordListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                switch (i) {
                    case KeyEvent.KEYCODE_ENTER:
                        if (checkPassword()) {
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Incorrect password!", Toast.LENGTH_SHORT).show();
                            setEnterPassword();
                        }
                        return true;
                    default:
                        break;
                }
            }
            return false;
        }
    };



    private void setEnterID() {
        tv.setText("Enter user identification");
        et.setText("");
        et.setOnKeyListener(checkIdListener);
        et.requestFocus();
        et.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 1);

    }

    private void setEnterPassword() {
        tv.setText("Enter password");
        et.setText("");
        et.setOnKeyListener(checkPasswordListener);
        et.requestFocus();
        et.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 1);
    }

    private boolean checkId() {
        //Firebase nonesense//
        return true;
    }
    private boolean checkPassword() {
        //Firebase nonsense//
        return true;
    }


    @Override
    public void onBackPressed() {
        setEnterID();
    }
}
