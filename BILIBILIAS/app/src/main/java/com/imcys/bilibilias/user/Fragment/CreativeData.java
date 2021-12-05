package com.imcys.bilibilias.user.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


import com.airbnb.lottie.L;
import com.imcys.bilibilias.HttpUtils;
import com.imcys.bilibilias.R;
import com.imcys.bilibilias.as.Reply;
import com.imcys.bilibilias.user.UserActivity;
import com.imcys.bilibilias.user.UserVideo;
import com.lihang.chart.ChartCircleItem;
import com.lihang.chart.ChartCircleView;
import com.lihang.chart.ChartLineItem;
import com.lihang.chart.ChartLineView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.SSLSession;

public class CreativeData extends Fragment {


    private String Mid;
    private String Cookie;
    private Intent intent;
    private List<UserVideo> fruitList = new ArrayList<>();
    private ProgressDialog pd2;
    private List<Reply> replyList = new ArrayList<>();
    private ChartLineView chartLineView;
    private ChartLineView chartLineUnFollow;
    private ChartLineView chartLineIncomeView;
    private ChartCircleView charCircleGender;
    private ChartCircleView charCircleAge;
    private ChartCircleView charCircleEquipment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.creative_data, container, false);

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        //获取首页用户信息变量
        Cookie = UserActivity.cookie;
        Mid = UserActivity.mid;

        initData();
        initDataCircle();

        new Thread(new Runnable() {
            @Override
            public void run() {
                String UserData = HttpUtils.doGet("https://member.bilibili.com/x/web/index/stat", Cookie);
                try {
                    JSONObject UserDataJson = new JSONObject(UserData);
                    if (UserDataJson.getInt("code") == 0) {
                        UserDataJson = UserDataJson.getJSONObject("data");
                        JSONObject fan_recent_thirty = UserDataJson.getJSONObject("fan_recent_thirty");
                        JSONObject follow = fan_recent_thirty.getJSONObject("follow");
                        JSONObject unfollow = fan_recent_thirty.getJSONObject("unfollow");
                        Iterator keys = follow.keys();
                        Iterator unfollowKeys = unfollow.keys();

                        chartLineView = (ChartLineView) getActivity().findViewById(R.id.chartLineView);
                        ArrayList<String> arrayList1 = new ArrayList<>();
                        ArrayList<Integer> points1 = new ArrayList<>();
                        ArrayList<Integer> points2 = new ArrayList<>();

                        points1.add(0);
                        while (keys.hasNext()) {
                            String key = String.valueOf(keys.next());
                            int y = follow.getInt(key);
                            points1.add(y);
                            arrayList1.add(key.substring(key.length() - 2) + "");
                        }

                        points2.add(0);
                        while (unfollowKeys.hasNext()) {
                            String key = String.valueOf(unfollowKeys.next());
                            int y = unfollow.getInt(key);
                            points2.add(y);
                        }

                        chartLineView.setHoriItems(arrayList1);
                        ArrayList<ChartLineItem> items1 = new ArrayList<>();
                        items1.add(new ChartLineItem(points1, R.color.colorAccent, "新增粉丝", true, true));
                        items1.add(new ChartLineItem(points2, R.color.textColor, "取关粉丝", true, true));
                        //设置折线数据源
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //设置折线数据源
                                chartLineView.setItems(items1);
                            }
                        });

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        loadIncome();
        loadFansData();

    }


    private void loadIncome() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String UserData = HttpUtils.doGet("https://api.bilibili.com/studio/growup/web/up/income/stat?type=0&s_locale=zh_CN", Cookie);
                try {
                    JSONObject UserDataJson = new JSONObject(UserData);
                    if (UserDataJson.getInt("code") == 0) {
                        chartLineIncomeView = (ChartLineView) getActivity().findViewById(R.id.chartLineIncomeView);
                        ArrayList<String> arrayList1 = new ArrayList<>();
                        ArrayList<Integer> points1 = new ArrayList<>();
                        JSONArray UserDataArray = UserDataJson.getJSONArray("data");
                        points1.add(0);
                        for (int i = 1; i < UserDataArray.length(); i++) {
                            JSONObject incomeJson = UserDataArray.getJSONObject(i);
                            int income = incomeJson.getInt("income");
                            Long date = incomeJson.getLong("date") * 1000;
                            points1.add(Math.round(income / 100));
                            SimpleDateFormat formatter = new SimpleDateFormat("dd");
                            String dateString = formatter.format(date);
                            arrayList1.add(dateString);
                        }
                        chartLineIncomeView.setHoriItems(arrayList1);
                        ArrayList<ChartLineItem> items1 = new ArrayList<>();
                        items1.add(new ChartLineItem(points1, R.color.colorAccent, "收益变化", true, true));
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //设置折线数据源
                                chartLineIncomeView.setItems(items1);
                            }
                        });

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    private void loadFansData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String FansData = HttpUtils.doGet("https://member.bilibili.com/x/web/data/base?tmid=" + Mid, Cookie);
                try {
                    JSONObject FansJson = new JSONObject(FansData);
                    if (FansJson.getInt("code") == 0) {
                        FansJson = FansJson.getJSONObject("data");
                        JSONObject viewer_base = FansJson.getJSONObject("viewer_base");
                        JSONObject fan = viewer_base.getJSONObject("fan");
                        int female = fan.getInt("female");
                        int male = fan.getInt("male");
                        int age_four = fan.getInt("age_four");
                        int age_one = fan.getInt("age_one");
                        int age_three = fan.getInt("age_three");
                        int age_two = fan.getInt("age_two");
                        int plat_android = fan.getInt("plat_android");
                        int plat_h5 = fan.getInt("plat_h5");
                        int plat_ios = fan.getInt("plat_ios");
                        int plat_other_app = fan.getInt("plat_other_app");
                        int plat_pc = fan.getInt("plat_pc");
                        int plat_out = fan.getInt("plat_out");


                        int finalPlat_android = plat_android;
                        int finalPlat_pc = plat_pc;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayList<ChartCircleItem> itemsFans = new ArrayList<>();
                                ArrayList<ChartCircleItem> itemsAge = new ArrayList<>();
                                ArrayList<ChartCircleItem> itemsEquipment = new ArrayList<>();

                                itemsFans.add(new ChartCircleItem(female, R.color.fansGreen, "女生"));
                                itemsFans.add(new ChartCircleItem(male, R.color.fansBlue, "男生"));


                                itemsAge.add(new ChartCircleItem(age_two, R.color.fansBlue, "16-25岁"));
                                itemsAge.add(new ChartCircleItem(age_four, R.color.fansGreen, "40岁以上"));
                                itemsAge.add(new ChartCircleItem(age_one, R.color.fansOrange, "0-16岁"));
                                itemsAge.add(new ChartCircleItem(age_three, R.color.fansPink, "25-40岁"));


                                itemsEquipment.add(new ChartCircleItem(plat_android, R.color.fansBlue, "移动设备"));
                                itemsEquipment.add(new ChartCircleItem(plat_pc, R.color.fansGreen, "PC设备"));
                                itemsEquipment.add(new ChartCircleItem(plat_other_app, R.color.fansOrange, "其他来源"));
                                itemsEquipment.add(new ChartCircleItem(plat_out, R.color.fansPink, "站外来源"));
                                //设置数据源
                                charCircleGender.setItems(itemsFans);
                                charCircleAge.setItems(itemsAge);
                                charCircleEquipment.setItems(itemsEquipment);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    private void initData() {
        chartLineView = (ChartLineView) getActivity().findViewById(R.id.chartLineView);
        chartLineIncomeView = (ChartLineView) getActivity().findViewById(R.id.chartLineIncomeView);

        //横坐标titles数据
        ArrayList<String> arrayList = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            arrayList.add("");
        }
        //设置横坐标titles
        chartLineView.setHoriItems(arrayList);
        chartLineIncomeView.setHoriItems(arrayList);

        //第一条折线数据
        ArrayList<Integer> points = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            points.add(i + 2);
        }
        ArrayList<ChartLineItem> items = new ArrayList<>();
        /*
         * 参数：
         * 1、折线统计的数据源
         * 2、此折线的颜色值
         * 3、手势操作后，展示此折线数据的描述语
         * 4、此折线是否带阴影填充色
         * 5、此折线是否带动画展示
         * */
        items.add(new ChartLineItem(points, R.color.textColor, "粉丝变化", true, true));
        //设置折线数据源
        chartLineView.setItems(items);
        chartLineIncomeView.setItems(items);

    }

    private void initDataCircle() {
        charCircleGender = (ChartCircleView) getActivity().findViewById(R.id.charCircleGender);
        charCircleAge = (ChartCircleView) getActivity().findViewById(R.id.charCircleAge);
        charCircleEquipment = (ChartCircleView) getActivity().findViewById(R.id.charCircleEquipment);
        ArrayList<ChartCircleItem> items = new ArrayList<>();
        /*
         * 参数：
         * 1、当前的value的值
         * 2、绘制此部分的颜色值
         * 3、此部分的文字描述
         * */
        items.add(new ChartCircleItem(1, R.color.colorAccent, "测试"));
        items.add(new ChartCircleItem(3, R.color.textColor, "测试"));
        //设置数据源
        charCircleAge.setItems(items);
        charCircleGender.setItems(items);
        charCircleEquipment.setItems(items);
    }

}
