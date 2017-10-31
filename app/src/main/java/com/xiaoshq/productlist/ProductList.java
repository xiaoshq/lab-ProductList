package com.xiaoshq.productlist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class ProductList extends AppCompatActivity {

    public RecyclerView mRecyclerView;
    public ListView mListView;
    public FloatingActionButton switchBTN;
    public boolean isShoppingCar;//false-mainpage; true-shoplist
    public app mApp;
    public Receiver staticReceiver;
    public Receiver dynamicReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);
        //注册订阅者
        EventBus.getDefault().register(this);

        //静态广播用bundle和intent发送广播
        Intent intentBroadcast1 = new Intent(Receiver.STATICACTION);
        intentBroadcast1.putExtra("itemid", new Random().nextInt(10));
        sendBroadcast(intentBroadcast1);

        //注册动态广播，实例化IntentFilter对象
        IntentFilter dynamicFilter = new IntentFilter();
        dynamicFilter.addAction(Receiver.DYNAMICACTION);
        dynamicReceiver = new Receiver();
        //注册广播接收
        registerReceiver(dynamicReceiver, dynamicFilter);

        mRecyclerView = (RecyclerView) findViewById(R.id.productList);
        mListView = (ListView) findViewById(R.id.shoppingList);
        mListView.setVisibility(View.GONE);
        switchBTN = (FloatingActionButton) findViewById(R.id.listSwitch);
        isShoppingCar = false;
        mApp = (app) getApplication();

        mRecyclerViewAdapter rcAdapter = new mRecyclerViewAdapter(ProductList.this,
                R.layout.product, new ArrayList<>(mApp.product_list.dataList),
                new ArrayList<>(mApp.product_list.productID));
        LinearLayoutManager LLManager = new LinearLayoutManager(ProductList.this);
        mRecyclerView.setLayoutManager(LLManager);
        LLManager.setOrientation(OrientationHelper.VERTICAL);
        mRecyclerView.setAdapter(rcAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        final mListViewAdapter lvAdapter = new mListViewAdapter(ProductList.this,
                mApp.shopping_list.dataList, mApp.shopping_list.productID);
        mListView.setAdapter(lvAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                if (pos != 0) {
                    Intent intent = new Intent();
                    intent.setClass(ProductList.this, ProductDetail.class);
                    intent.putExtra("itemid", mApp.shopping_list.productID.get(pos));//传参
                    startActivity(intent);
                }
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int pos, long id) {
                if (pos != 0) {
                    AlertDialog.Builder ad_builder;
                    ad_builder = new AlertDialog.Builder(ProductList.this);
                    ad_builder
                            .setTitle("移除商品")
                            .setMessage("从购物车移除"
                                    + mApp.shopping_list.dataList.get(pos).get("name").toString()
                                    + "？")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    mApp.shopping_list.dataList.remove(pos);
                                    mApp.shopping_list.productID.remove(pos);
                                    lvAdapter.notifyDataSetChanged();
                                }
                            }).create().show();
                }
                return true;
            }
        });


        switchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShoppingCar == false) {//mainpage->shoplist
                    isShoppingCar = true;
                    mRecyclerView.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    switchBTN.setImageResource(R.drawable.mainpage);
                } else if (isShoppingCar == true) {//shoplist->mainpage
                    isShoppingCar = false;
                    mListView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    switchBTN.setImageResource(R.drawable.shoplist);
                }
            }
        });

    }

    public interface mOnItemClickListener {//定义listener

        void onClick(int pos);

        void onLongClick(int pos);
    }

    public class mRecyclerViewAdapter extends RecyclerView.Adapter<mRecyclerViewAdapter.mRecyclerViewHolder> {

        ArrayList<Map<String, Object>> data;
        ArrayList<Integer> dataID;
        Context context;
        int layoutID;
        mOnItemClickListener clkListener;

        public mRecyclerViewAdapter(Context ctx, int layoutId,
                                    final ArrayList<Map<String, Object>> _data, final ArrayList<Integer> dataId) {
            context = ctx;
            layoutID = layoutId;
            data = _data;
            dataID = dataId;

            this.clkListener = new mOnItemClickListener() {
                @Override
                public void onClick(int pos) {
                    Intent intent = new Intent();
                    intent.setClass(ProductList.this, ProductDetail.class);
                    intent.putExtra("itemid", dataID.get(pos));
                    startActivity(intent);
                }

                @Override
                public void onLongClick(int pos) {
                    dataID.remove(pos);
                    data.remove(pos);
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, data.size());
                    int realpos = pos + 1;
                    Toast.makeText(context, "移除第" + realpos + "个商品", Toast.LENGTH_SHORT).show();
                }
            };
        }

        public void convert(mRecyclerViewHolder viewholder, Map<String, Object> map) {//把自定义的内容取出来
            TextView firstLetter = viewholder.getView(R.id.firstLetter);
            TextView name = viewholder.getView(R.id.name);
            TextView price = viewholder.getView(R.id.price);
            firstLetter.setText(map.get("firstLetter").toString());
            name.setText(map.get("name").toString());
            price.setText("");
        }

        public class mRecyclerViewHolder extends RecyclerView.ViewHolder {
            public SparseArray<View> v_dump; // cache.
            public View v_this;


            public mRecyclerViewHolder(View view) {
                super(view);
                v_this = view;
                v_dump = new SparseArray<>();
            }

            public <T extends View> T getView(int viewID) {
                View view = v_dump.get(viewID);
                if (view == null) {
                    view = v_this.findViewById(viewID);
                    v_dump.put(viewID, view);
                }
                return (T) view;
            }
        }

        public mRecyclerViewHolder makemRecyclerViewHolder(Context context, ViewGroup vgrp, int layoutID) {
            return new mRecyclerViewHolder(LayoutInflater.from(context).inflate(layoutID, vgrp, false));
        }

        @Override
        public void onBindViewHolder(final mRecyclerViewHolder viewholder, int pos) {
            convert(viewholder, data.get(pos));
            if (clkListener != null) {
                viewholder.v_this.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clkListener.onClick(viewholder.getAdapterPosition());
                    }
                });
                viewholder.v_this.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        clkListener.onLongClick(viewholder.getAdapterPosition());
                        return true;
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        @Override
        public mRecyclerViewHolder onCreateViewHolder(ViewGroup vgrp, int viewType) {
            return makemRecyclerViewHolder(context, vgrp, layoutID);
        }
    }

    public class mListViewAdapter extends BaseAdapter {
        ArrayList<Map<String, Object>> data;
        ArrayList<Integer> dataID;
        Context context;

        public mListViewAdapter(Context ctx, ArrayList<Map<String, Object>> _data, ArrayList<Integer> dataId) {
            data = _data;
            dataID = dataId;
            context = ctx;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Map<String, Object> getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public class mListViewHolder {
            public TextView firstLetter;
            public TextView name;
            public TextView price;
        }

        @Override
        public View getView(int i, View view, ViewGroup vgrp) {
            View temp;
            mListViewHolder lvHolder;
            if (view == null) {
                temp = LayoutInflater.from(context).inflate(R.layout.product, null);
                lvHolder = new mListViewHolder();
                lvHolder.firstLetter = temp.findViewById(R.id.firstLetter);
                lvHolder.name = temp.findViewById(R.id.name);
                lvHolder.price = temp.findViewById(R.id.price);
                temp.setTag(lvHolder);
            } else {
                temp = view;
                lvHolder = (mListViewHolder) temp.getTag();
            }
            lvHolder.firstLetter.setText(data.get(i).get("firstLetter").toString());
            lvHolder.name.setText(data.get(i).get("name").toString());
            lvHolder.price.setText(data.get(i).get("price").toString());

            return temp;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(dynamicReceiver);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.msg.equals("refresh")) {
            ((mListViewAdapter) mListView.getAdapter()).notifyDataSetChanged();
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (intent.hasExtra("isShoppingCar")) {//false-mainpage; true-shoplist
            isShoppingCar = intent.getBooleanExtra("isShoppingCar", false);
            if (isShoppingCar == false) {//mainpage->shoplist
                isShoppingCar = true;
                mRecyclerView.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
                switchBTN.setImageResource(R.drawable.mainpage);
            } else if (isShoppingCar == true) {//shoplist->mainpage
                isShoppingCar = false;
                mListView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                switchBTN.setImageResource(R.drawable.shoplist);
            }

        }
    }

}
