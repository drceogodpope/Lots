package com.heapdragon.lots;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Francesco on 2017-04-24.
 */

public class TestViewActivity extends AppCompatActivity {
    private RelativeLayout root;
    private LotDot lotDot;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        root = (RelativeLayout) findViewById(R.id.testview_root);
        lotDot = (LotDot) findViewById(R.id.lotDot);
    }
}
