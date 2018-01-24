package cn.sn.zwcx.yizi.activitys;


import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sn.zwcx.sdk.base.BaseCompatActivity;
import cn.sn.zwcx.yizi.R;

public class QRCodeActivity extends BaseCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;

    private CardView share;


    @Override
    protected void initView(Bundle savedInstanceState) {
        toolbar = findViewById(R.id.toolbar);
        share = findViewById(R.id.cv_share);

        share.setOnClickListener(this);

        initTitleBar(toolbar,getResources().getString(R.string.download_str));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_qrcode;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_share:
                showShare();
                break;
        }
    }

    /** 显示分享 */
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("Yizhi");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("https://fir.im/s4lr");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("每日新闻，精选干货，最新资讯，应有尽有.下载链接：https://fir.im/s4lr");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        //由于微信需要注册AppKey才能演示，这里取消微信分享，个人根据自己的需求注册Appkey使用
        oks.setUrl("https://fir.im/s4lr");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("这个App贼好用，快下载体验吧~");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("https://fir.im/s4lr");
        // 启动分享GUI
        oks.show(this);

    }
}
