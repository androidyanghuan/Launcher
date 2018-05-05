package cn.sn.zwcx.mvvm.activitys;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.io.File;

import cn.sn.zwcx.mvvm.R;
import cn.sn.zwcx.mvvm.constants.Constant;
import cn.sn.zwcx.mvvm.databinding.ActivityImageBrowseBinding;
import cn.sn.zwcx.mvvm.utils.FileUtil;
import cn.sn.zwcx.mvvm.utils.StatusBarUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ImageBrowseActivity extends AppCompatActivity {
    private final String TAG = ImageBrowseActivity.class.getSimpleName();

    private ActivityImageBrowseBinding mBinding;

    private String mImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 分解式
    //    getWindow().setEnterTransition(new Explode());
    //    getWindow().setExitTransition(new Explode());
        // 滑动进入
    //    getWindow().setEnterTransition(new Slide());
    //    getWindow().setExitTransition(new Slide());
        // 淡入淡出
     //   getWindow().setEnterTransition(new Fade());
     //   getWindow().setExitTransition(new Fade());
        StatusBarUtil.setColor(this, Color.BLACK);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_image_browse);
        mImageUrl = Constant.MAIN_NV_USER_ICON;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mImageUrl = extras.getString("url");
            if (mImageUrl.endsWith(".gif"))
                loadGif();
            else
                loadImage();
        }

        mBinding.activityImageBrowsePv.enable();
        mBinding.activityImageBrowseToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  finish();
                onBackPressed();
            }
        });

        mBinding.activityImageBrowseFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage();
            }
        });

    }

    private void saveImage() {
        Observable.create(new ObservableOnSubscribe<File>() {
            @Override
            public void subscribe(ObservableEmitter<File> e) throws Exception {
                e.onNext(Glide.with(ImageBrowseActivity.this)
                .load(mImageUrl)
                .downloadOnly(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL, com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
                .get());
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) throws Exception {
                        saveImage(file);
                    }
                });
    }

    private void saveImage(File file) {
        FileUtil.saveImage(this,mImageUrl,file, new FileUtil.SaveResultCallback() {
            @Override
            public void onSavedSuccess() {
                Snackbar.make(mBinding.activityImageBrowsePv,R.string.save_success,Snackbar.LENGTH_LONG)
                        .setActionTextColor(ContextCompat.getColor(ImageBrowseActivity.this,android.R.color.white)).show();
            }

            @Override
            public void onSavedFailed(final String error) {
                if (error.contains("open failed: ENOENT (No such file or directory)"))
                    Snackbar.make(mBinding.activityImageBrowsePv,R.string.grant_storage_permission,Snackbar.LENGTH_LONG)
                            .setActionTextColor(ContextCompat.getColor(ImageBrowseActivity.this,android.R.color.white)).show();
                else
                    Snackbar.make(mBinding.activityImageBrowsePv,R.string.save_failed,Snackbar.LENGTH_LONG)
                        .setActionTextColor(ContextCompat.getColor(ImageBrowseActivity.this,android.R.color.white)).show();
            }
        });
    }

    private void loadGif() {
        Glide.with(this)
                .load(mImageUrl)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new GlideDrawableImageViewTarget(mBinding.activityImageBrowsePv){
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        mBinding.activityImageBrowsePb.setVisibility(View.GONE);
                    }
                });
    }

    private void loadImage() {
        Glide.with(this)
                .load(mImageUrl)
                .fitCenter()
                .crossFade()
                .into(new GlideDrawableImageViewTarget(mBinding.activityImageBrowsePv){
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        mBinding.activityImageBrowsePb.setVisibility(View.GONE);
                    }
                });
    }

}
