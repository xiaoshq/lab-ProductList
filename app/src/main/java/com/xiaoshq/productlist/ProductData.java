package com.xiaoshq.productlist;

import android.support.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by ivan on 2017/10/23.
 */

public class ProductData {
    public ArrayList<Map<String, Object>> dataList;
    public ArrayList<Integer> collect;
    public ArrayList<Integer> productID;
    String[] name = {"Enchated Forest", "Arla Milk", "Devondale Milk", "Kindle Oasis",
            "waitrose 早餐麦片", "Mcvitie's 饼干", "Ferrero Rocher", "Maltesers", "Lindt",
            "Borggreve"};
    String[] price = {"¥5.00", "¥59.00", "¥79.00", "¥2399.00", "¥179.00", "¥14.90",
            "¥132.59", "¥141.43", "¥139.43", "¥28.90"};
    String[] type = {"作者", "产地", "产地", "版本", "重量", "产地", "重量", "重量", "重量",
            "重量"};
    String[] info = {"Johanna Basford", "德国", "澳大利亚", "8GB", "2Kg", "英国", "300g",
            "118g", "249g", "640g"};
    String[] firstLetter = {"E", "A", "D", "K", "W", "M", "F", "M", "L", "B"};
    Integer[] img = {R.drawable.enchated_forest, R.drawable.arla, R.drawable.devondale,
            R.drawable.kindle, R.drawable.waitrose, R.drawable.mcvitie, R.drawable.ferrero,
            R.drawable.maltesers, R.drawable.lindt, R.drawable.borggreve};

    public ProductData(boolean listType) {//初始化商品列表和购物车列表
        dataList = new ArrayList<>();
        collect = new ArrayList<>();
        productID = new ArrayList<>();
        if (listType) {//product list
            for (int i=0; i<name.length; i++) {
                Map<String, Object> dataMap = new LinkedHashMap<>();
                collect.add(0);
                productID.add(i);
                dataMap.put("name", name[i]);
                dataMap.put("price", price[i]);
                dataMap.put("type", type[i]);
                dataMap.put("info", info[i]);
                dataMap.put("firstLetter", firstLetter[i]);
                dataMap.put("img", img[i]);
                dataList.add(dataMap);
            }
        } else {//shopping list
            Map<String, Object> dataMap = new LinkedHashMap<>();
            collect.add(0);
            productID.add(-1);
            dataMap.put("name", "购物车");
            dataMap.put("price", "价格");
            dataMap.put("type", null);
            dataMap.put("info", null);
            dataMap.put("firstLetter", "*");
            dataMap.put("img", null);
            dataList.add(dataMap);
        }
    }

    public void addShoppingList(Map<String, Object> map, int p_id) {
        dataList.add(map);
        productID.add(p_id);
    }

    public int idGetIndex(int p_id) {
        return productID.indexOf(p_id);
    }
}
