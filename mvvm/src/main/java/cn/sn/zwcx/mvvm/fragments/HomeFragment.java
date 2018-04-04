package cn.sn.zwcx.mvvm.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.sn.zwcx.mvvm.R;
import cn.sn.zwcx.mvvm.adapters.HomeFragmentAdapter;
import cn.sn.zwcx.mvvm.databinding.HomeFragmentBinding;
import cn.sn.zwcx.mvvm.fragments.home.WangyiFragment;
import cn.sn.zwcx.mvvm.fragments.home.WeixinFragment;
import cn.sn.zwcx.mvvm.fragments.home.ZhihuFragment;

/**
 * Created by on 2018/3/5 16:31.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class HomeFragment extends Fragment {

    private final String TAG = HomeFragment.class.getSimpleName();

    private HomeFragmentBinding binding;

    private String mTitle;

    private List<Fragment> pages;

    public interface OnOpenDrawerLayoutListener{
        void onOpen();
    }

    private OnOpenDrawerLayoutListener onOpenDrawerLayoutListener;

    public static HomeFragment newInstance(String title){
        HomeFragment homeFragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("title",title);
        homeFragment.setArguments(args);
        return homeFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOpenDrawerLayoutListener)
            onOpenDrawerLayoutListener = (OnOpenDrawerLayoutListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mTitle = getArguments().getString("title");
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment,container,false);
        binding.homeFragmentToolbar.inflateMenu(R.menu.home_fragment_toolbar_menu);
        binding.homeFragmentTitle.setText(mTitle);
        binding.homeFragmentToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOpenDrawerLayoutListener != null);
                    onOpenDrawerLayoutListener.onOpen();
            }
        });
        initViews();
        return binding.getRoot();
    }

    private void initViews() {
        pages = new ArrayList<>();
        pages.add(ZhihuFragment.newInstance());
        pages.add(WangyiFragment.newInstance());
        pages.add(WeixinFragment.newInstance());

        // 实例化适配器
        HomeFragmentAdapter adapter = new HomeFragmentAdapter(getActivity().getSupportFragmentManager(),pages,getActivity(),R.array.home_fragment_tab);
        // 给ViewPager设置适配器
        binding.homeFragmentVp.setAdapter(adapter);
        binding.homeFragmentVp.setCurrentItem(View.SCROLLBAR_POSITION_DEFAULT);
        binding.homeFragmentVp.setOffscreenPageLimit(View.SCROLLBAR_POSITION_RIGHT);// ViewPage预加载2页
        // 把TabLayout和ViewPager关联起来
        binding.homeFragmentTl.setupWithViewPager(binding.homeFragmentVp);
        binding.homeFragmentTl.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_DEFAULT);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home_fragment_toolbar_menu,menu);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onOpenDrawerLayoutListener = null;
    }
}
