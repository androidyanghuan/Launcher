package cn.sn.zwcx.mvvm.fragments;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import cn.sn.zwcx.mvvm.R;
import cn.sn.zwcx.mvvm.activitys.MovieDetailActivity;
import cn.sn.zwcx.mvvm.adapters.MovieAdapter;
import cn.sn.zwcx.mvvm.apis.DoubanApi;
import cn.sn.zwcx.mvvm.base.MyObserver;
import cn.sn.zwcx.mvvm.bean.douban.HotMovieBean;
import cn.sn.zwcx.mvvm.bean.douban.child.Subjects;
import cn.sn.zwcx.mvvm.databinding.FragmentMovieBinding;
import cn.sn.zwcx.mvvm.databinding.FragmentMovieItemHeadBinding;
import cn.sn.zwcx.mvvm.utils.MLog;
import cn.sn.zwcx.mvvm.utils.RetrofitFactory;
import cn.sn.zwcx.mvvm.utils.ToastUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by on 2018/4/12 15:50.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class MovieFragment extends Fragment implements View.OnClickListener {
    private final String TAG = MovieFragment.class.getSimpleName();

    private FragmentMovieBinding mBinding;

    private Context mContext;

    private MovieAdapter mAdapter;

    private List<Subjects> mMovies;

    private RefreshLayout mRefresh,mLoadMore;

    private int mStart = 0;

    public static MovieFragment newInstance(String title){
        Bundle args = new Bundle();
        args.putString("title",title);
        MovieFragment fragment = new MovieFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie,container,false);
        mContext = getActivity();
        String title = getArguments().getString("title");
        mBinding.fragmentMoveTitleTv.setText(title);
        initView();
        getData(true,0);
        return mBinding.getRoot();
    }

    private void getData(boolean isShowDialog,int start) {
        RetrofitFactory.getInstance().createApi(DoubanApi.class,DoubanApi.HOST)
                .getHotMovieList(start,10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<HotMovieBean>(mContext,isShowDialog) {
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (isNetWorkError)
                            ToastUtil.showToast(R.string.network_error);
                        if (mRefresh != null) {
                            mRefresh.finishRefresh(false);
                            mRefresh = null;
                        }
                        if (mLoadMore != null) {
                            mLoadMore.finishLoadMore(false);
                            mLoadMore = null;
                        }
                    }

                    @Override
                    protected void onSuccees(HotMovieBean hotMovieBean) throws Exception {
                        if (mRefresh != null) {
                            mRefresh.finishRefresh(true);
                            mRefresh = null;
                            mMovies.clear();
                        } else if (mLoadMore != null) {
                            mLoadMore.finishLoadMore(true);
                            mLoadMore = null;
                        }
                        mMovies.addAll(hotMovieBean.getSubjects());
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void initView() {
        mMovies = new ArrayList<>();
        mBinding.fragmentMoveRv.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.fragmentMoveRv.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MovieAdapter(R.layout.fragment_movie_item,mMovies);
        FragmentMovieItemHeadBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),R.layout.fragment_movie_item_head,null,false);
        binding.setTop(new HotMovieBean());
        binding.executePendingBindings();
        binding.fragmentMovieItemHeader.setOnClickListener(this);
        mAdapter.addHeaderView(binding.getRoot());
        mBinding.fragmentMoveRv.setAdapter(mAdapter);
        String url = "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=457017329,3408705623&fm=27&gp=0.jpg";
        Glide.with(mContext).load(url).crossFade(300).placeholder(R.drawable.img_default_movie).into(binding.fragmentMovieSubHeadIcon);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ImageView icon = view.findViewById(R.id.fragment_movie_item_icon);
                Subjects subjects = (Subjects) adapter.getItem(position);
                Intent intent = new Intent(mContext, MovieDetailActivity.class);
                intent.putExtra("subjects", subjects);
                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(getActivity(),icon,getString(R.string.transition_movie_img));
                startActivity(intent,options.toBundle());
            }
        });
        mBinding.fragmentMovieSrl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mLoadMore = refreshLayout;
                mStart += 10;
                getData(false,mStart + 1);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mRefresh = refreshLayout;
                mStart = 0;
                getData(false,0);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_movie_item_header:
                if (onFragmentCreated != null)
                    onFragmentCreated.onFragmentCreate(true);
                break;
        }
    }

    public interface OnFragmentCreated{
        void onFragmentCreate(boolean isShowing);
    }

    private OnFragmentCreated onFragmentCreated;

    public void setOnFragmentCreated(OnFragmentCreated onFragmentCreated) {
        this.onFragmentCreated = onFragmentCreated;
    }

}
