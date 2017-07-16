package com.heapdragon.lots;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import java.util.ArrayList;

public class LotIntervalFragment extends Fragment {
    private static final String TAG = "LotIntervalFragment";
    protected LinearLayout intervalLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static LotIntervalFragment newInstance() {
        return new LotIntervalFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView()");
        View view = inflater.inflate(R.layout.fragment_lot_interval,container,false);
        intervalLayout = (LinearLayout) view.findViewById(R.id.intervalLayout);
        IntervalLine firstLine = new IntervalLine(getContext());
        firstLine.delete.setVisibility(View.INVISIBLE);
        intervalLayout.addView(firstLine);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public ArrayList<LotInterval> getLotIntervals(){
        ArrayList<LotInterval> lotIntervals = new ArrayList<>();
        for(int i = 0; i <intervalLayout.getChildCount();i++) {
            IntervalLine intervalLine = (IntervalLine) intervalLayout.getChildAt(i);
            intervalLine.getIntervalBounds();
            lotIntervals.add(intervalLine.getIntervalBounds());
        }
        return lotIntervals;
    }

    public Object[] validateLotIntervals(ArrayList<LotInterval> lotIntervals) {
        clearTints();
        Object[] result = new Object[2];
        result[1] = 0;
        long lastM = 0;
        for (int i = 0; i < lotIntervals.size(); i++) {
            result[1] = i;
            long n = lotIntervals.get(i).getN();
            long m = lotIntervals.get(i).getM();
            if (n<=0 || n>=m || m>=10000 || n<=lastM) {
                result[0] = false;
                showError(result);
                return result;
            }
            lastM = m;
        }
        result[0] = true;
        return result;
    }

    private void showError(Object[] validation) {
        clearTints();
        IntervalLine intervalLine = (IntervalLine) intervalLayout.getChildAt((int)validation[1]);
        int colorInt = getResources().getColor(R.color.colorRed);
        ColorStateList csl = ColorStateList.valueOf(colorInt);
        intervalLine.n.setBackgroundTintList(csl);
        intervalLine.m.setBackgroundTintList(csl);
        YoYo.with(Techniques.Shake).duration(500).playOn(intervalLine);
        Toast.makeText(getContext(),"Invalid lot interval!",Toast.LENGTH_SHORT).show();
    }

    private void clearTints(){
        for (int i = 0; i < intervalLayout.getChildCount(); i++) {
            IntervalLine intervalLine = (IntervalLine) intervalLayout.getChildAt(i);
            int colorInt = getResources().getColor(R.color.colorWhite);
            ColorStateList csl = ColorStateList.valueOf(colorInt);
            intervalLine.n.setBackgroundTintList(csl);
            intervalLine.m.setBackgroundTintList(csl);
        }
    }

    private class IntervalLine extends LinearLayout{
        private ImageButton delete;
        private ImageButton add;
        private EditText n;
        private EditText m;

        public IntervalLine(Context context) {
            super(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.fragment_interval_line, this);
            n = (EditText) v.findViewById(R.id.n_lots);
            m = (EditText) v.findViewById(R.id.m_lots);
            add = (ImageButton) v.findViewById(R.id.add_line);
            delete = (ImageButton) v.findViewById(R.id.remove_line);
            add.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if(intervalLayout.getChildCount()<5){
                        intervalLayout.addView(new IntervalLine(getContext()));
                    }
                }
            });
            delete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    intervalLayout.removeView(IntervalLine.this);
                }
            });
        }
        public LotInterval getIntervalBounds(){
            int nBound = Integer.valueOf(n.getText().toString());
            int mBound = Integer.valueOf(m.getText().toString());
            return new LotInterval(nBound, mBound);
        }
    }


}
