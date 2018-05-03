package cn.sn.zwcx.mvvm.activitys;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.sn.zwcx.mvvm.BR;
import cn.sn.zwcx.mvvm.R;
import cn.sn.zwcx.mvvm.adapters.BaseAdapter;
import cn.sn.zwcx.mvvm.apis.DoubanApi;
import cn.sn.zwcx.mvvm.base.MyObserver;
import cn.sn.zwcx.mvvm.bean.douban.DirectorBean;
import cn.sn.zwcx.mvvm.bean.douban.MovieDetailBean;
import cn.sn.zwcx.mvvm.bean.douban.child.Avatars;
import cn.sn.zwcx.mvvm.bean.douban.child.Casts;
import cn.sn.zwcx.mvvm.bean.douban.child.Directors;
import cn.sn.zwcx.mvvm.bean.douban.child.Subjects;
import cn.sn.zwcx.mvvm.databinding.ActivityMovieDetailBinding;
import cn.sn.zwcx.mvvm.utils.RetrofitFactory;
import cn.sn.zwcx.mvvm.utils.StatusBarUtil;
import cn.sn.zwcx.mvvm.utils.ToastUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailActivity extends AppCompatActivity {
    private final String TAG = MovieDetailActivity.class.getSimpleName();

    private ActivityMovieDetailBinding mBinding;

    private BaseAdapter mAdapter;

    private List<DirectorBean> mDirector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_movie_detail);
        initView();
    }

    private void initView() {
        if (getIntent() == null)
            return;
        Subjects subjects = (Subjects) getIntent().getSerializableExtra("subjects");
        if (subjects == null)
            return;
        mBinding.setSubject(subjects);
        mBinding.executePendingBindings();
        getData(false,subjects.getId());
        setSupportActionBar(mBinding.activityMovieDetailToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mBinding.activityMovieDetailToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    finish();
                onBackPressed();
            }
        });

        int headerBgHeight;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            headerBgHeight = mBinding.activityMovieDetailToolbar.getLayoutParams().height + StatusBarUtil.getStatusBarHeight(this);
            mBinding.activityMovieDetail.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }else
            headerBgHeight = mBinding.activityMovieDetailToolbar.getLayoutParams().height;
        // 使背景图向上移动到图片的最低端，保留（toolbar+状态栏）的高度
        // 实际上此时ivToolbarBg高度还是330dp，只是除了toolbar外，剩下部分是透明状态
        ViewGroup.MarginLayoutParams ivTitleHeadBgParams = (ViewGroup.MarginLayoutParams)
                mBinding.ivToolbarBg.getLayoutParams();
        int marginTop = mBinding.ivToolbarBg.getLayoutParams().height - headerBgHeight;
        ivTitleHeadBgParams.setMargins(0, -marginTop, 0, 0);
        mBinding.activityMovieDetailRv.setLayoutManager(new LinearLayoutManager(this));
        mBinding.activityMovieDetailRv.setItemAnimator(new DefaultItemAnimator());
        mDirector = new ArrayList<>();
        mAdapter = new BaseAdapter(R.layout.activity_movie_detail_item_end, BR.director,mDirector);
        mBinding.activityMovieDetailRv.setAdapter(mAdapter);
        mBinding.activityMovieDetailRv.setNestedScrollingEnabled(false);
        mBinding.activityMovieDetailCnsv.bindAlphaView(mBinding.ivToolbarBg);
    }

    private void getData(boolean isShowDialog, String id) {
        RetrofitFactory.getInstance().createApi(DoubanApi.class,DoubanApi.HOST)
                .getMovieDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<MovieDetailBean>(this,isShowDialog) {
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (isNetWorkError)
                            ToastUtil.showToast(R.string.network_error);
                    }

                    @Override
                    protected void onSuccees(MovieDetailBean movieDetailBean) throws Exception {
                        if (movieDetailBean == null)
                            return;
                        mBinding.setMovieDetail(movieDetailBean);
                        initDirectorActorData(movieDetailBean);
                        mBinding.executePendingBindings();

                    }
                });
    }

    private void initDirectorActorData(MovieDetailBean movieDetailBean) {
        List<DirectorBean> directorList = new ArrayList<>();
        List<Directors> directors = movieDetailBean.getDirectors();
        int directorSize = directors.size();
        for (int i = 0; i < directorSize; i++) {
            String alt = directors.get(i).getAlt();
            String type = "导演";
            Avatars avatars = directors.get(i).getAvatars();
            String name = directors.get(i).getName();
            String id = directors.get(i).getId();
            DirectorBean director = new DirectorBean(alt,type,avatars,name,id);
            directorList.add(director);
        }
        List<Casts> casts = movieDetailBean.getCasts();
        int size = casts.size();
        for (int i = 0; i < size; i++) {
            String altActor = casts.get(i).getAlt();
            String typeActor = "演员";
            Avatars avatarsActor = casts.get(i).getAvatars();
            String nameActor = casts.get(i).getName();
            String idActor = casts.get(i).getId();
            DirectorBean directorActor = new DirectorBean(altActor,typeActor,avatarsActor,nameActor,idActor);
            directorList.add(directorActor);
        }
        mDirector.addAll(directorList);
        mAdapter.notifyDataSetChanged();
    }
}
