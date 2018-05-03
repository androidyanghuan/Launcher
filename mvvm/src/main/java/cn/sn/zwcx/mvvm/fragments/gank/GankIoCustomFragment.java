package cn.sn.zwcx.mvvm.fragments.gank;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cocosw.bottomsheet.BottomSheet;

import java.util.ArrayList;
import java.util.List;

import cn.sn.zwcx.mvvm.R;
import cn.sn.zwcx.mvvm.adapters.GankIoCustomAdapter;
import cn.sn.zwcx.mvvm.adapters.HeaderAndFooterWrapper;
import cn.sn.zwcx.mvvm.apis.GankioApi;
import cn.sn.zwcx.mvvm.base.MyObserver;
import cn.sn.zwcx.mvvm.bean.gankio.GankIoCustomListBean;
import cn.sn.zwcx.mvvm.bean.gankio.GankIoItemBean;
import cn.sn.zwcx.mvvm.databinding.GankIoFragmentCustomBinding;
import cn.sn.zwcx.mvvm.databinding.GankIoFragmentCustomHeadBinding;
import cn.sn.zwcx.mvvm.utils.RetrofitFactory;
import cn.sn.zwcx.mvvm.utils.ToastUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by on 2018/4/3 16:16.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 * https://github.com/soarcn/BottomSheet
 */

public class GankIoCustomFragment extends Fragment {
    private final String TAG = GankIoCustomFragment.class.getSimpleName();

    private Context mContext;

    private GankIoFragmentCustomBinding mBinding;

    private boolean isCreated = false;

    private String mType = "all";

    private GankIoCustomAdapter mAdapter;

    private List<GankIoItemBean> mItems;

    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;

    private GankIoFragmentCustomHeadBinding headBinding;

    public static GankIoCustomFragment newInstance(){
        Bundle args = new Bundle();
        GankIoCustomFragment fragment = new GankIoCustomFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCreated = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.gank_io_fragment_custom,container,false);
        mContext = getActivity();
        initViews();
        getData(true,mType);

        FingerprintManagerCompat fingerprint = FingerprintManagerCompat.from(mContext);
        boolean hardwareDetected = fingerprint.isHardwareDetected();
        if (hardwareDetected)
            Log.e(TAG,"当前设备支持指纹解锁...");
        else
            Log.e(TAG,"当前设备不支持指纹解锁!!!");
        boolean fingerprints = fingerprint.hasEnrolledFingerprints();
        if (fingerprints)
            Log.e(TAG,"当前设备已保存过指纹信息....");
        else
            Log.e(TAG,"当前设备暂未保存任何指纹信息!!!");
        fingerprint.authenticate(null, 0, new CancellationSignal(), new FingerprintManagerCompat.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                super.onAuthenticationError(errMsgId, errString);
                handlerMessage(errMsgId);
            }
        },null);


        return mBinding.getRoot();
    }

    private void handlerMessage(int errMsgId) {
        switch (errMsgId) {
            case FingerprintManager.FINGERPRINT_ERROR_CANCELED:
                Log.e(TAG,"指纹传感器不可用...");
                break;
            case FingerprintManager.FINGERPRINT_ERROR_HW_UNAVAILABLE:
                Log.e(TAG,"当前设备不可用...");
                break;
            case FingerprintManager.FINGERPRINT_ERROR_LOCKOUT:
                Log.e(TAG,"错误指纹验证多次导致设备被锁...");
                break;
            case FingerprintManager.FINGERPRINT_ERROR_NO_SPACE:
                Log.e(TAG,"没有足够的存储空间保存本次操作....");
                break;
            case FingerprintManager.FINGERPRINT_ERROR_TIMEOUT:
                Log.e(TAG,"操作时间太长,一般为30秒");
                break;
            case FingerprintManager.FINGERPRINT_ERROR_UNABLE_TO_PROCESS:
                Log.e(TAG,"传感器无法处理当前指纹图片....");
                break;
        }
    }

    private void initViews() {
        mItems = new ArrayList<>();
        mAdapter = new GankIoCustomAdapter(mItems);
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
        headBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext),R.layout.gank_io_fragment_custom_head,null,false);
        View footView = LayoutInflater.from(mContext).inflate(R.layout.gank_io_fragment_custom_foot,null);
        mHeaderAndFooterWrapper.addHeaderView(headBinding.getRoot());
        mHeaderAndFooterWrapper.addFootView(footView);
        mBinding.gankIoCustomRv.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.gankIoCustomRv.setItemAnimator(new DefaultItemAnimator());
        mBinding.gankIoCustomRv.setAdapter(mHeaderAndFooterWrapper);
        headBinding.gankIoCustomTypeTv.setText(mType);
        headBinding.gankIoCustomClassifyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BottomSheet.Builder(getActivity(),R.style.BottomSheet_StyleDialog).title(R.string.selected_classify)
                        .sheet(R.menu.gank_io_custom_bottom_sheet)
                        .listener(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case R.id.item_gank_all:
                                        mType = "all";
                                        break;
                                    case R.id.item_gank_app:
                                        mType = "App";
                                        break;
                                    case R.id.item_gank_android:
                                        mType = "Android";
                                        break;
                                    case R.id.item_gank_ios:
                                        mType = "iOS";
                                        break;
                                    case R.id.item_gank_front:
                                        mType = "前端";
                                        break;
                                    case R.id.item_gank_video:
                                        mType = "休息视频";
                                        break;
                                    case R.id.item_gank_tuozhan:
                                        mType = "拓展资源";
                                        break;
                                }
                                headBinding.gankIoCustomTypeTv.setText(mType);
                                getData(true,mType);
                            }
                        }).show();
            }
        });
    }

    private void getData(boolean isShowDialog, String type) {
        RetrofitFactory.getInstance().createApi(GankioApi.class,GankioApi.HOST)
                .getGankIoCustomList(type,10,1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<GankIoCustomListBean>(mContext,isShowDialog) {
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (isNetWorkError)
                            ToastUtil.showToast(R.string.network_error);
                    }

                    @Override
                    protected void onSuccees(GankIoCustomListBean gankIoCustomListBean) throws Exception {
                        if (mItems != null && mItems.size() > 0)
                        mItems.clear();
                        mItems.addAll(gankIoCustomListBean.getResults());
                        mHeaderAndFooterWrapper.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isCreated)
            return;
        if (isVisibleToUser) {
            mType = getArguments().getString("type");
            getData(true,mType);
            if (!TextUtils.isEmpty(mType))
                headBinding.gankIoCustomTypeTv.setText(mType);
        }
    }

}
