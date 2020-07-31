package com.example.mymhchart.model;

import java.util.ArrayList;

public class OneTimeData {
    private float mPrice;
    private float mEveragePrice;
    private float mStrength;

    public OneTimeData(float mPrice, float mEveragePrice, float mStrength) {
        this.mPrice = mPrice;
        this.mEveragePrice = mEveragePrice;
        this.mStrength = mStrength;
    }

    public float getmPrice() {
        return mPrice;
    }

    public float getmEveragePrice() {
        return mEveragePrice;
    }

    public float getmStrength() {
        return mStrength;
    }

    public float findByLocation(int i) {
        if ( i == 0) {
            return mPrice;
        }else if (i == 1) {
            return mEveragePrice;
        }else {
            return mStrength;
        }
    }

}
