package cn.sn.zwcx.yizi.model.gankio.tabs;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import cn.sn.zwcx.sdk.base.BaseModel;
import cn.sn.zwcx.sdk.config.DBConfig;
import cn.sn.zwcx.sdk.config.ItemState;
import cn.sn.zwcx.sdk.helper.RetrofitCreateHelper;
import cn.sn.zwcx.sdk.helper.RxHelper;
import cn.sn.zwcx.sdk.utils.AppUtil;
import cn.sn.zwcx.sdk.utils.DBUtil;
import cn.sn.zwcx.yizi.api.GankioApi;
import cn.sn.zwcx.yizi.contract.gankio.tabs.GankIoDayContract;
import cn.sn.zwcx.yizi.model.bean.gankio.GankIoDayBean;
import cn.sn.zwcx.yizi.model.bean.gankio.GankIoDayItemBean;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;

/**
 * Created by on 2018/1/23 11:57.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class GankIoDayModel extends BaseModel implements GankIoDayContract.IGankIoDayModel {

    private GankIoDayBean mGankIoDayBean;

    @NonNull
    public static GankIoDayModel newInstance(){
        return new GankIoDayModel();
    }

    @Override
    public Observable<Boolean> recordItemIsRead(final String key) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(DBUtil.newInstance(AppUtil.getContext()).insertRead(DBConfig.TABLE_GANKIO_DAY,key, ItemState.STATE_IS_READ));
                e.onComplete();
            }
        }).compose(RxHelper.<Boolean>rxSchedulerHelper());
    }

    @Override
    public Observable<List<GankIoDayItemBean>> getGankIoDayList(String year, String month, String day) {
        return RetrofitCreateHelper.createApi(GankioApi.class,GankioApi.HOST)
                .getGankIoDay(year,month,day)
                .map(new Function<GankIoDayBean, List<GankIoDayItemBean>>() {
                    @Override
                    public List<GankIoDayItemBean> apply(GankIoDayBean gankIoDayBean) throws Exception {
                        mGankIoDayBean = gankIoDayBean;
                        List<GankIoDayItemBean> list = new ArrayList<>();
                        //增加item类型
                        GankIoDayItemBean itemAndroidBean = gankIoDayBean.getResults().getAndroid().get(0);
                        GankIoDayItemBean itemIOSBean = gankIoDayBean.getResults().getiOS().get(0);
                        GankIoDayItemBean itemFrontBean = gankIoDayBean.getResults().getFront().get(0);
                        GankIoDayItemBean itemWelfareBean = gankIoDayBean.getResults().getWelfare().get(0);
                        GankIoDayItemBean itemRestMovieBean = gankIoDayBean.getResults().getRestMovie().get(0);
                        itemAndroidBean.itemType = GankIoDayItemBean.GANK_IO_DAY_ITEM_DAY_REFESH;
                        itemIOSBean.itemType = GankIoDayItemBean.GANK_IO_DAY_ITEM_DAY_REFESH;
                        itemFrontBean.itemType = GankIoDayItemBean.GANK_IO_DAY_ITEM_DAY_NORMAL;
                        itemWelfareBean.itemType = GankIoDayItemBean.GANK_IO_DAY_ITEM_DAY_NORMAL;
                        itemRestMovieBean.itemType = GankIoDayItemBean.GANK_IO_DAY_ITEM_DAY_NORMAL;
                        list.add(itemAndroidBean);
                        list.add(itemIOSBean);
                        list.add(itemFrontBean);
                        list.add(itemWelfareBean);
                        list.add(itemRestMovieBean);
                        return list;
                    }
                }).compose(RxHelper.<List<GankIoDayItemBean>>rxSchedulerHelper());
    }

    @Override
    public GankIoDayItemBean getGankIoDayAndroid(int page) {
        if (mGankIoDayBean == null)
            return null;
        GankIoDayItemBean bean = mGankIoDayBean.getResults().getAndroid().get(page);
        bean.itemType = GankIoDayItemBean.GANK_IO_DAY_ITEM_DAY_REFESH;
        return bean;
    }

    @Override
    public GankIoDayItemBean getGankIoDayIOS(int page) {
        if (mGankIoDayBean == null)
            return null;
        GankIoDayItemBean bean = mGankIoDayBean.getResults().getiOS().get(page);
        bean.itemType = GankIoDayItemBean.GANK_IO_DAY_ITEM_DAY_REFESH;
        return bean;
    }
}
