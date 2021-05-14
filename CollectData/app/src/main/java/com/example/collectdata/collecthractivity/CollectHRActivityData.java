package com.example.collectdata.collecthractivity;

import android.content.Context;

import com.samsung.android.sdk.accessory.SA;
import com.samsung.android.sdk.accessory.SAAgentV2;

public class CollectHRActivityData extends SAAgentV2 {

    private Context context;

    public CollectHRActivityData(String s, Context context) {
        super(s, context);
        this.context = context;
    }
}
