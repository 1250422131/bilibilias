package com.imcys.bilibilias.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.imcys.bilibilias.HttpUtils;
import com.imcys.bilibilias.R;
import com.imcys.bilibilias.as.VideoAsActivity;
import com.kongzue.dialogx.dialogs.CustomDialog;
import com.kongzue.dialogx.interfaces.OnBindView;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class ServerDonationActivity extends AppCompatActivity {
    private String AliPay;
    private String WeChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_donation);

        newTool();

        ProgressDialog pd2 = ProgressDialog.show(ServerDonationActivity.this, "提示", "正在获取服务器数据");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String ServerStr = HttpUtils.doGet("https://api.misakamoe.com/app/AppFunction.php?type=Donate", "");
                try {
                    JSONObject ServerJson = new JSONObject(ServerStr);
                    int Surplus = ServerJson.getInt("Surplus");
                    int Total = ServerJson.getInt("Total");
                    WeChat = ServerJson.getString("WeChat");
                    AliPay = ServerJson.getString("Alipay");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //double除法运算
                            DecimalFormat df = new DecimalFormat("0.00");
                            double progress = Double.valueOf(df.format((float) Surplus / Total));
                            progress = (int) mul(progress, 100);
                            ImageView WeChatImage = (ImageView) findViewById(R.id.Server_WeChat);
                            ImageView AliPayImage = (ImageView) findViewById(R.id.Server_AliPay);
                            ProgressBar ProgressBar = (ProgressBar) findViewById(R.id.Server_ProgressBar);
                            TextView DonateTextView = (TextView) findViewById(R.id.Server_Donate_TextView);
                            ProgressBar.setProgress((int) progress);
                            DonateTextView.setText(Surplus + "/" + Total + "￥");

                            Glide.with(ServerDonationActivity.this)
                                    .load("https://i0.hdslb.com/bfs/im/eb661ebb971978a080ec7bc352e97a81b54dc581.png")
                                    .into(AliPayImage);
                            Glide.with(ServerDonationActivity.this)
                                    .load("https://i0.hdslb.com/bfs/im/9c83edbaeed4a431ae581c29327f2ff3a35d0293.png")
                                    .into(WeChatImage);
                            pd2.cancel();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static double mul(double v1, double v2) {

        BigDecimal b1 = new BigDecimal(Double.toString(v1));

        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.multiply(b2).doubleValue();

    }

    private void newTool() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.Server_Toolbar);
        mToolbar.setTitle("服务器捐助");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //设置Menu点击事件
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return true;

    }


    public void openWeChatImage(View view) {
        CustomDialog.build()
                .setAlign(CustomDialog.ALIGN.BOTTOM)
                .setAutoUnsafePlacePadding(false)
                .setCustomView(new OnBindView<CustomDialog>(R.layout.layout_dialogx_miui_as) {
                    @Override
                    public void onBind(final CustomDialog dialog, View v) {
                        TextView btnOk;
                        ImageView mImageView;
                        mImageView = v.findViewById(R.id.image_pay);
                        btnOk = v.findViewById(R.id.btn_selectPositive);
                        Glide.with(ServerDonationActivity.this)
                                .load(WeChat)
                                .into(mImageView);
                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .show();
    }

    public void openAliPayImage(View view) {
        CustomDialog.build()
                .setAlign(CustomDialog.ALIGN.BOTTOM)
                .setAutoUnsafePlacePadding(false)
                .setCustomView(new OnBindView<CustomDialog>(R.layout.layout_dialogx_miui_as) {
                    @Override
                    public void onBind(final CustomDialog dialog, View v) {
                        TextView btnOk;
                        ImageView mImageView;
                        mImageView = v.findViewById(R.id.image_pay);
                        btnOk = v.findViewById(R.id.btn_selectPositive);
                        Glide.with(ServerDonationActivity.this)
                                .load(AliPay)
                                .into(mImageView);
                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .show();
    }


}
