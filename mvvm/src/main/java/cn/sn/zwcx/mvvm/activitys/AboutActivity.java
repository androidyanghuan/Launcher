package cn.sn.zwcx.mvvm.activitys;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;

import cn.sn.zwcx.mvvm.R;
import cn.sn.zwcx.mvvm.databinding.ActivityAboutBinding;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = AboutActivity.class.getSimpleName();

    private ActivityAboutBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_about);
        mBinding.activityAboutToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mBinding.activityAboutSdv.setImageURI(Uri.parse("res://"+ getPackageName() + File.separator + R.mipmap.ic_launcher));
        mBinding.activityAboutAuthor.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_about_author:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://github.com/androidyanghuan"));
                startActivity(intent);
                break;
        }
    }
}
