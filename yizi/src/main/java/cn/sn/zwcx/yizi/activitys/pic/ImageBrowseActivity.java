package cn.sn.zwcx.yizi.activitys.pic;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.io.File;
import java.lang.annotation.Target;
import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sn.zwcx.sdk.base.BaseCompatActivity;
import cn.sn.zwcx.sdk.helper.RxHelper;
import cn.sn.zwcx.sdk.utils.FileUtil;
import cn.sn.zwcx.sdk.utils.StatusBarUtil;
import cn.sn.zwcx.yizi.R;
import cn.sn.zwcx.yizi.constants.BundleKeyConstant;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

public class ImageBrowseActivity extends BaseCompatActivity {

    @BindView(R.id.pv_pic)
    PhotoView pvPic;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pb_progress_bar)
    ProgressBar progressBar;

    private String mImageUrl;

    @Override
    protected void initData() {
        super.initData();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        mImageUrl = bundle.getString(BundleKeyConstant.ARG_KEY_IMAGE_BROWSE_URL);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initTitleBar(toolbar,"");
        StatusBarUtil.setColor(this, Color.BLACK);
        pvPic.enable();
        if (mImageUrl.contains("gif"))
            loadGif();
        else
            loadImage();
    }

    /** 加载静态图片 */
    private void loadImage() {
        Glide.with(context)
                .load(mImageUrl)
                .fitCenter()
                .crossFade()
                .into(new GlideDrawableImageViewTarget(pvPic){
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        // 在这里添加图片加载完成的动作
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    /** 加载gif动图 */
    private void loadGif() {
        Glide.with(context)
                .load(mImageUrl)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new GlideDrawableImageViewTarget(pvPic){
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        // 在这里添加图片加载完成的动作
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_browse;
    }

    @OnClick(R.id.fab_save_pic)
    public void onClick(View view){
        int id = view.getId();
        switch (id){
            case R.id.fab_save_pic:
                saveImage();
                break;
        }
    }

    /** 保存图片到本地 */
    private void saveImage() {
        io.reactivex.Observable.create(new ObservableOnSubscribe<File>() {
            @Override
            public void subscribe(ObservableEmitter<File> e) throws Exception {
                e.onNext(Glide.with(context)
                .load(mImageUrl)
                .downloadOnly(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL, com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
                .get());
                e.onComplete();
            }
        }).compose(RxHelper.<File>rxSchedulerHelper()).subscribe(new Consumer<File>() {
            @Override
            public void accept(File file) throws Exception {
                saveImage(file);
            }
        });
    }

    /**
     * 保存图片并且回调提示
     * @param file
     */
    private void saveImage(File file) {
        FileUtil.saveImage(context, mImageUrl, file, new FileUtil.SaveResultCallback() {
            @Override
            public void onSavedSuccess() {
                Snackbar.make(pvPic,R.string.save_success,Snackbar.LENGTH_LONG).setActionTextColor(ContextCompat.getColor(context,android.R.color.white)).show();
            }

            @Override
            public void onSavedFailed() {
                Snackbar.make(pvPic,R.string.save_failed,Snackbar.LENGTH_LONG).setActionTextColor(ContextCompat.getColor(context,android.R.color.white)).show();
            }
        });
    }

}
