package com.ryandzhunter.contact.util;

import android.content.Context;

/**
 * Created by ryandzhunter on 8/2/17.
 */

public class Preferences {

    private static final String IS_SHOULD_INVALIDATE_LIST = "IsShouldInvalidateList";
    private static final String IS_SHOULD_RELOAD_LIST = "IsShouldReloadList";
    private final Context mContext;

    public Preferences(Context context) {
        this.mContext = context;
    }

    public boolean isShouldInvalidateList() {
        return PreferenceHelper.getBoolean(mContext, IS_SHOULD_INVALIDATE_LIST, false);
    }

    public void setIsShouldInvalidateList(boolean isShouldInvalidateList) {
        PreferenceHelper.putBoolean(mContext, IS_SHOULD_INVALIDATE_LIST, isShouldInvalidateList);
    }

    public boolean isShouldReloadList(){
        return PreferenceHelper.getBoolean(mContext, IS_SHOULD_RELOAD_LIST, false);
    }

    public void setIsShouldReloadList(boolean isShouldReloadList){
        PreferenceHelper.putBoolean(mContext, IS_SHOULD_RELOAD_LIST, isShouldReloadList);
    }

}
