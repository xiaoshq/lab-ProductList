package com.xiaoshq.productlist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class ProductDetail extends AppCompatActivity {

    app my_app;
    TextView t_price;
    TextView t_name;
    TextView t_type;
    TextView t_info;
    Button b_addchart;
    Button b_back;
    Button b_star;
    ImageView i_img;
    TextView lv_more;
    ListView lv_op;
    int idx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);

        my_app = (app) getApplication();
        int iid = getIntent().getIntExtra("itemid", 0);

        idx = my_app.p_list.idGetIndex(iid);
        t_price = (TextView) findViewById(R.id.detail_price);
        t_name = (TextView) findViewById(R.id.name);
        t_type = (TextView) findViewById(R.id.detail_type);
        t_info = (TextView) findViewById(R.id.detail_info);
        b_addchart = (Button) findViewById(R.id.add2car);
        b_back = (Button) findViewById(R.id.back);
        b_star = (Button) findViewById(R.id.star);
        i_img = (ImageView) findViewById(R.id.productImg);
        lv_more = (TextView) findViewById(R.id.moreInfo);
        lv_op = (ListView) findViewById(R.id.detail_listview);

        t_price.setText(my_app.p_list.dataList.get(idx).get("price").toString());
        t_name.setText(my_app.p_list.dataList.get(idx).get("name").toString());
        t_type.setText(my_app.p_list.dataList.get(idx).get("type").toString());
        t_info.setText(my_app.p_list.dataList.get(idx).get("info").toString());
        i_img.setImageResource(my_app.p_list.img[idx]);

        b_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        update_star();
        b_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int val = my_app.p_list.collect.get(idx);
                if (val == 1) val = 0;
                else val = 1;
                my_app.p_list.collect.set(idx, val);
                update_star();
            }
        });
        b_addchart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my_app.s_list.addShoppingList(
                        my_app.p_list.dataList.get(idx),
                        my_app.p_list.productID.get(idx)
                );
                Toast.makeText(ProductDetail.this,
                        "商品已经添加到购物车", Toast.LENGTH_SHORT).show();
            }
        });
        final String[] s_op = {"一键下单", "分享产品", "不感兴趣", "查看更多产品促销消息"};
        lv_op.setAdapter(new ArrayAdapter<>(ProductDetail.this, R.layout.detail_option, s_op));
    }

    public void update_star() {
        if (my_app.p_list.collect.get(idx) == 0) {
            b_star.setBackgroundResource(R.drawable.empty_star);
        } else {
            b_star.setBackgroundResource(R.drawable.full_star);
        }
    }

}
