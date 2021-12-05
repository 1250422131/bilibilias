package com.imcys.bilibilias.user.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.imcys.bilibilias.HttpUtils;
import com.imcys.bilibilias.R;
import com.imcys.bilibilias.as.Reply;
import com.imcys.bilibilias.as.VideoAsActivity;
import com.imcys.bilibilias.user.UserActivity;
import com.imcys.bilibilias.user.UserVideo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.samlss.broccoli.Broccoli;
import me.samlss.broccoli.BroccoliGradientDrawable;
import me.samlss.broccoli.PlaceholderParameter;

public class CreativeHome extends Fragment {


    private String Mid;
    private String Cookie;

    private TextView CreativeFansNum;
    private TextView CreativePlayNum;
    private TextView CreativeReplyNum;
    private TextView CreativeLikeNum;
    private TextView CreativeFansUpNum;
    private TextView CreativePlayUpNum;
    private TextView CreativeReplyUpNum;
    private TextView CreativeLikeUpNum;
    Broccoli broccoli;
    private TextView CreativeIncome;
    private TextView CreativeIncomeUp;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.creative_home, container, false);

        return view;
    }

    private void binDingControl() {
        CreativeFansNum = (TextView) getActivity().findViewById(R.id.Creative_FansNum);
        CreativeLikeNum = (TextView) getActivity().findViewById(R.id.Creative_LikeNum);
        CreativeReplyNum = (TextView) getActivity().findViewById(R.id.Creative_ReplyNum);
        CreativePlayNum = (TextView) getActivity().findViewById(R.id.Creative_PlayNum);
        CreativeFansUpNum = (TextView) getActivity().findViewById(R.id.Creative_FansUpNum);
        CreativeLikeUpNum = (TextView) getActivity().findViewById(R.id.Creative_LikeUpNum);
        CreativeReplyUpNum = (TextView) getActivity().findViewById(R.id.Creative_ReplyUpNum);
        CreativePlayUpNum = (TextView) getActivity().findViewById(R.id.Creative_PlayUpNum);
        CreativeIncome = (TextView) getActivity().findViewById(R.id.Creative_Income);
        CreativeIncomeUp = (TextView) getActivity().findViewById(R.id.Creative_IncomeUp);
        broccoli = new Broccoli();
        TextView listTextView[] = {CreativeFansNum, CreativeLikeNum, CreativeReplyNum, CreativePlayNum, CreativeFansUpNum, CreativeLikeUpNum, CreativeReplyUpNum, CreativePlayUpNum, CreativeIncome, CreativeIncomeUp};
        for (int i = 0; i < listTextView.length; i++) {
            broccoli.addPlaceholder(new PlaceholderParameter.Builder()
                    .setView(listTextView[i])
                    .setDrawable(new BroccoliGradientDrawable(Color.parseColor("#DDDDDD"),
                            Color.parseColor("#CCCCCC"), 0, 1000, new LinearInterpolator()))
                    .build());
        }
        broccoli.show();


    }


    @Override
    public void onStart() {
        super.onStart();
        //获取首页用户信息变量
        Cookie = UserActivity.cookie;
        Mid = UserActivity.mid;
        binDingControl();
        loadData();
        loadIncome();
    }

    private void loadData() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String UserData = HttpUtils.doGet("https://member.bilibili.com/x/web/index/stat", Cookie);
                try {
                    JSONObject UserDataJson = new JSONObject(UserData);
                    if (UserDataJson.getInt("code") == 0) {
                        UserDataJson = UserDataJson.getJSONObject("data");
                        int total_click = UserDataJson.getInt("total_click");
                        int total_like = UserDataJson.getInt("total_like");
                        int total_fans = UserDataJson.getInt("total_fans");
                        int total_reply = UserDataJson.getInt("total_reply");
                        int incr_reply = UserDataJson.getInt("incr_reply");
                        int incr_fans = UserDataJson.getInt("incr_fans");
                        int incr_click = UserDataJson.getInt("incr_click");
                        int inc_like = UserDataJson.getInt("incr_reply");

                        getActivity().runOnUiThread(() -> {
                            CreativeLikeNum.setText(total_like + "");
                            CreativeReplyNum.setText(total_reply + "");
                            CreativePlayNum.setText(total_click + "");
                            CreativeFansNum.setText(total_fans + "");
                            CreativeFansUpNum.setText("+" + incr_fans);
                            CreativeLikeUpNum.setText("+" + inc_like);
                            CreativeReplyUpNum.setText("+" + incr_reply);
                            CreativePlayUpNum.setText("+" + incr_click);
                        });

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void loadIncome() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String UserData = HttpUtils.doGet("https://api.bilibili.com/studio/growup/web/up/wallet/summary?s_locale=zh_CN", Cookie);
                try {
                    JSONObject UserDataJson = new JSONObject(UserData);
                    if (UserDataJson.getInt("code") == 0) {
                        UserDataJson = UserDataJson.getJSONObject("data");
                        int unwithdraw_income = UserDataJson.getInt("unwithdraw_income");
                        int day_income = UserDataJson.getInt("day_income");

                        String income = unwithdraw_income + "";
                        String substring;
                        if (income.length() >= 2) {
                            substring = income.substring(income.length() - 2);
                        } else {
                            substring = "0.00";
                        }

                        String new_day_income = day_income + "";
                        String substring1;
                        if (income.length() >= 2) {
                            substring1 = new_day_income.substring(new_day_income.length() - 2);
                        } else {
                            substring1 = "0.00";
                        }

                        final String final_new_day_income = new_day_income.replaceFirst(substring1, "." + substring1);

                        final String final_income = income.replaceFirst(substring, "." + substring);
                        getActivity().runOnUiThread(() -> {
                            CreativeIncome.setText(final_income);
                            CreativeIncomeUp.setText(final_new_day_income);
                            broccoli.clearAllPlaceholders();
                        });

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
