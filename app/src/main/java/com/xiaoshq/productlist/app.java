package com.xiaoshq.productlist;

import android.app.Application;

/**
 * Created by ivan on 2017/10/23.
 */

public class app extends Application {
    public ProductData p_list;
    public ProductData s_list;

    @Override
    public void onCreate() {
        super.onCreate();
        p_list = new ProductData(true);
        s_list = new ProductData(false);
    }
}
