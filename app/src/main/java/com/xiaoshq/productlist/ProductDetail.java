package com.xiaoshq.productlist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import org.greenrobot.eventbus.EventBus;

public class ProductDetail extends AppCompatActivity {

    app mApp;
    TextView price;
    TextView name;
    TextView type;
    TextView info;
    Button addtocar;
    Button back;
    Button star;
    ImageView productImg;
    TextView moreInfo;
    ListView oprations;
    int idx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);

        mApp = (app) getApplication();
        final int iid = getIntent().getIntExtra("itemid", 0);

        idx = mApp.product_list.idGetIndex(iid);
        price = (TextView) findViewById(R.id.detail_price);
        name = (TextView) findViewById(R.id.name);
        type = (TextView) findViewById(R.id.detail_type);
        info = (TextView) findViewById(R.id.detail_info);
        addtocar = (Button) findViewById(R.id.add2car);
        back = (Button) findViewById(R.id.back);
        star = (Button) findViewById(R.id.star);
        productImg = (ImageView) findViewById(R.id.productImg);
        moreInfo = (TextView) findViewById(R.id.moreInfo);
        oprations = (ListView) findViewById(R.id.detail_listview);

        price.setText(mApp.product_list.dataList.get(idx).get("price").toString());
        name.setText(mApp.product_list.dataList.get(idx).get("name").toString());
        type.setText(mApp.product_list.dataList.get(idx).get("type").toString());
        info.setText(mApp.product_list.dataList.get(idx).get("info").toString());
        productImg.setImageResource(mApp.product_list.img[idx]);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        update_star();
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int val = mApp.product_list.collect.get(idx);
                if (val == 1) val = 0;
                else val = 1;
                mApp.product_list.collect.set(idx, val);
                update_star();
            }
        });
        addtocar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mApp.shopping_list.addShoppingList(
                        mApp.product_list.dataList.get(idx),
                        mApp.product_list.productID.get(idx)
                );
                EventBus.getDefault().post(new MessageEvent("refresh"));
                Toast.makeText(ProductDetail.this,
                        "商品已经添加到购物车", Toast.LENGTH_SHORT).show();
                Intent intentBroadcast = new Intent(Receiver.DYNAMICACTION);//发送动态广播
                intentBroadcast.putExtra("itemid", iid);
                sendBroadcast(intentBroadcast);
            }
        });
        final String[] s_op = {"一键下单", "分享产品", "不感兴趣", "查看更多产品促销消息"};
        oprations.setAdapter(new ArrayAdapter<>(ProductDetail.this, R.layout.detail_option, s_op));
    }

    public void update_star() {
        if (mApp.product_list.collect.get(idx) == 0) {
            star.setBackgroundResource(R.drawable.empty_star);
        } else {
            star.setBackgroundResource(R.drawable.full_star);
        }
    }


}
