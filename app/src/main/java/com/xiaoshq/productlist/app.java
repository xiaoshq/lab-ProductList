package com.xiaoshq.productlist;

import android.app.Application;

/**
 * Created by ivan on 2017/10/23.
 */

public class app extends Application {
    public ProductData product_list;
    public ProductData shopping_list;
    public ProductList.mListViewAdapter lvadapter;

    @Override
    public void onCreate() {
        super.onCreate();
        product_list = new ProductData(true);
        shopping_list = new ProductData(false);
    }
}
