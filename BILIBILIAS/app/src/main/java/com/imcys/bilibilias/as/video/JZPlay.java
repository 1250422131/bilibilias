package com.imcys.bilibilias.as.video;


import android.content.Context;


import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.imcys.bilibilias.HttpUtils;
import com.imcys.bilibilias.R;


import cn.jzvd.JzvdStd;

public class JZPlay extends JzvdStd {


    public JZPlay(Context context) {
        super(context);
    }

    public JZPlay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.jz_layout_std;
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.poster) {
            //Toast.makeText(getContext(),"播放",Toast.LENGTH_SHORT).show();
            clickPoster();
        } else if (i == R.id.surface_container) {
            clickSurfaceContainer();
            if (clarityPopWindow != null) {
                clarityPopWindow.dismiss();
            }
        } else if (i == R.id.back) {
            clickBack();
        } else if (i == R.id.back_tiny) {
            clickBackTiny();
        } else if (i == R.id.clarity) {
            clickClarity();
        } else if (i == R.id.retry_btn) {
            clickRetryBtn();
        }
    }


    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        changeUiToComplete();
        cancelDismissControlViewTimer();
        bottomProgressBar.setProgress(100);
        //Toast.makeText(getContext(),"播放完成",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCompletion() {
        super.onCompletion();
        cancelDismissControlViewTimer();
    }





}
