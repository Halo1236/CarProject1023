package com.testservice.manager;

import android.content.Context;

public class BTServiceManager {

    public interface OnServiceStateLister{

        void onChangedState(boolean isConnected);
    }

    private Context mCtx;


    private static BTServiceManager _INSTATNCE;

    public static BTServiceManager getInstance(){
        if(null == _INSTATNCE){
            _INSTATNCE = new BTServiceManager();
        }
        return _INSTATNCE;
    }

    public void attachContext(Context ctx){
        this.mCtx = ctx;
    }


}
