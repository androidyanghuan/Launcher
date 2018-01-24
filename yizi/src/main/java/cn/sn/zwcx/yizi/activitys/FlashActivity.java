package cn.sn.zwcx.yizi.activitys;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.concurrent.TimeUnit;

import cn.sn.zwcx.sdk.base.BaseCompatActivity;
import cn.sn.zwcx.sdk.helper.RxHelper;
import cn.sn.zwcx.sdk.utils.ToastUtil;
import cn.sn.zwcx.yizi.R;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class FlashActivity extends BaseCompatActivity implements View.OnClickListener {
    private final String TAG = FlashActivity.class.getSimpleName();

    private LinearLayout skip;

    private TextView countDown;

    private boolean isCancle;
    private int time = 5;

    @Override
    protected void initView(Bundle savedInstanceState) {
           skip = findViewById(R.id.ll_skip);
           countDown = findViewById(R.id.tv_count_down);

           skip.setOnClickListener(this);
        //注：魅族pro6s-7.0-flyme6权限没有像类似6.0以上手机一样正常的提示dialog获取运行时权限，而是直接默认给了权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions();
        }

    }

    /**
     * 请求app运行权限
     */
    private void requestPermissions() {
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (!aBoolean)
                            ToastUtil.showToast(R.string.permission_info);
                        //不管是否获取全部权限，进入主页面
                        initCountDown();
                    }
                });
    }

    /**
     * 初始化倒计时
     */
    private void initCountDown() {
        Observable.interval(1, TimeUnit.SECONDS)
                .take(5)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return time - aLong;
                    }
                })
                .compose(RxHelper.<Long>rxSchedulerHelper())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long value) {
                        countDown.setText(String.valueOf(value));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        if (!isCancle) {
                            startActivity(new Intent(FlashActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_flash;
    }

    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
        isCancle = true;
        setIsTransAnim(false);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_skip:
                isCancle = true;
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }
}
