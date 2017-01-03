package com.heapdragon.lots;

/**
 * Created by Francesco on 2017-01-02.
 */

public enum ModelObject {

    LOTS(R.string.lots,R.layout.fragment_lots),
    MAP(R.string.map, R.layout.fragment_map),
    LOG(R.string.log, R.layout.fragment_log);

    private int mTitleResId;
    private int mLayoutResId;

    ModelObject(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }
}
