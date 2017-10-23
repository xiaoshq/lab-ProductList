package com.xiaoshq.productlist;

import android.content.Context;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class ProductList extends AppCompatActivity {

    public RecyclerView mRecyclerView;
    public ListView mListView;
    public app mApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);

        mApp = (app) getApplication();
        mRecyclerView = (RecyclerView) findViewById(R.id.productLIst);

        mRecyclerViewAdapter rcAdapter = new mRecyclerViewAdapter(ProductList.this,
                R.layout.product, new ArrayList<>(mApp.p_list.dataList),
                new ArrayList<>(mApp.p_list.productID));
        LinearLayoutManager leanerLayoutManager = new LinearLayoutManager(ProductList.this);
        mRecyclerView.setLayoutManager(leanerLayoutManager);
        leanerLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecyclerView.setAdapter(rcAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public interface mOnItemClickListener {
        void onClick(int pos);
        void onLongClick(int pos);
    }

    public class mRecyclerViewAdapter extends RecyclerView.Adapter<mRecyclerViewAdapter.mRecyclerViewHolder> {

        ArrayList<Map<String, Object>> data;
        ArrayList<Integer> dataID;
        Context context;
        int layoutID;
        mOnItemClickListener clkListener;

        public mRecyclerViewAdapter(Context ctx, int layoutID,
                                    final ArrayList<Map<String,Object>> data, final ArrayList<Integer> dataID) {
            this.context = ctx;
            this.layoutID = layoutID;
            this.data = data;
            this.dataID = dataID;

            this.clkListener = new mOnItemClickListener() {
                @Override
                public void onClick(int pos) {

                }

                @Override
                public void onLongClick(int pos) {
                    dataID.remove(pos);
                    data.remove(pos);
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, data.size());
                    Toast.makeText(context, "移除第" + pos + "个商品", Toast.LENGTH_SHORT).show();
                }
            };
        }

        public void convert(mRecyclerViewHolder viewholder, Map<String, Object> map) {
            TextView firstLetter = viewholder.getView(R.id.firstLetter);
            TextView name = viewholder.getView(R.id.name);
            TextView price = viewholder.getView(R.id.price);
            firstLetter.setText(map.get("firstLetter").toString());
            name.setText(map.get("name").toString());
            price.setText("");
        }

        public class mRecyclerViewHolder extends RecyclerView.ViewHolder {
            public SparseArray<View> v_dump;
            public View v_this;


            public mRecyclerViewHolder(View view) {
                super(view);
                v_this = view;
                v_dump = new SparseArray<>();
            }

            public <T extends View> T getView(int viewID) {
                View view = v_dump.get(viewID);
                if(view == null) {
                    view = v_this.findViewById(viewID);
                    v_dump.put(viewID,view);
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



}

