package cn.sn.zwcx.mvvm.fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import cn.sn.zwcx.mvvm.R;
import cn.sn.zwcx.mvvm.activitys.MovieDetailActivity;
import cn.sn.zwcx.mvvm.adapters.TopMovieAdapter;
import cn.sn.zwcx.mvvm.apis.DoubanApi;
import cn.sn.zwcx.mvvm.base.MyObserver;
import cn.sn.zwcx.mvvm.bean.douban.HotMovieBean;
import cn.sn.zwcx.mvvm.bean.douban.child.Subjects;
import cn.sn.zwcx.mvvm.databinding.FragmentTopMovieBinding;
import cn.sn.zwcx.mvvm.utils.MLog;
import cn.sn.zwcx.mvvm.utils.RetrofitFactory;
import cn.sn.zwcx.mvvm.utils.ToastUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by on 2018/4/19 10:00.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class TopMovieFragment extends Fragment {
    private final String TAG = TopMovieFragment.class.getSimpleName();

    private FragmentTopMovieBinding mBinding;

    private List<Subjects> mItems;

    private TopMovieAdapter mAdapter;

    private RefreshLayout mLoadMore;
    private int mStart = 0;

    public static TopMovieFragment newInstance(String title){
        Bundle args = new Bundle();
        args.putString("title",title);
        TopMovieFragment fragment = new TopMovieFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_top_movie,container,false);
        String title = getArguments().getString("title");
        mBinding.fragmentTopMovieToolbar.setTitle(title);
        mBinding.fragmentTopMovieToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onFragmentDismiss != null)
                    onFragmentDismiss.onFragmentDismiss(true);
            }
        });
        initView();
        getData(0,true);
        return mBinding.getRoot();
    }

    private void getData(int start, boolean isShowing) {
        RetrofitFactory.getInstance().createApi(DoubanApi.class,DoubanApi.HOST)
                .getMovieTop250(start,20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<HotMovieBean>(getActivity(),isShowing) {
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (isNetWorkError)
                            ToastUtil.showToast(R.string.network_error);
                        if (mLoadMore != null)
                            mLoadMore.finishLoadMore(false);
                    }

                    @Override
                    protected void onSuccees(HotMovieBean hotMovieBean) throws Exception {
                        if (mLoadMore != null)
                            mLoadMore.finishLoadMore(true);
                        else if (hotMovieBean != null && hotMovieBean.getSubjects() != null)
                            mItems.clear();
                        mItems.addAll(hotMovieBean.getSubjects());
                        mAdapter.notifyDataSetChanged();

                    }
                });
    }

    private void initView() {
        mItems = new ArrayList<>();
        mBinding.fragmentTopMovieRv.setLayoutManager(new GridLayoutManager(getActivity(),3));
        mBinding.fragmentTopMovieRv.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new TopMovieAdapter(mItems);
        mAdapter.setOnItemClickListener(new TopMovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                ImageView imageView = view.findViewById(R.id.fragment_top_movie_item_icon);
                Subjects subjects = mItems.get(position);
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                intent.putExtra("subjects",subjects);
                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(getActivity(), imageView, getString(R.string.transition_movie_img));
                startActivity(intent,options.toBundle());
            }
        });
        mBinding.fragmentTopMovieRv.setAdapter(mAdapter);
        mBinding.fragmentTopMovieSfl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                MLog.LogE(TAG,"加载更多....");
                mLoadMore = refreshLayout;
                mStart += 20;
                getData(mStart, true);
            }
        });
    }

    public interface OnFragmentDismiss{
        void onFragmentDismiss(boolean isShowing);
    }

    private OnFragmentDismiss onFragmentDismiss;

    public void setOnFragmentDismiss(OnFragmentDismiss onFragmentDismiss) {
        this.onFragmentDismiss = onFragmentDismiss;
    }

}
