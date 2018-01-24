package cn.sn.zwcx.yizi.contract.home.tabs;

import cn.sn.zwcx.yizi.contract.gankio.tabs.BaseTabsContract;
import cn.sn.zwcx.yizi.model.bean.weixin.WeixinChoiceItemBean;
import cn.sn.zwcx.yizi.model.bean.weixin.WeixinChoiceListBean;
import io.reactivex.Observable;

/**
 * Created by on 2018/1/15 11:59.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public interface WeixinContract {
    abstract class WeixinPresenter extends BaseTabsContract.BaseTabsPresenter<IWeixinModel,IWeixinView,WeixinChoiceItemBean>{

    }

    interface IWeixinModel extends BaseTabsContract.IBaseTabsModel{
        /**
         * 获取微信精选
         * @param page          指定微信精选页数->空的话默认1
         * @param pageSize      每页显示条数->空的话默认20条
         * @param dttype        返回数据的格式,xml或json，空的话->默认json
         * @param key           聚合key
         * @return              Observable
         */
        Observable<WeixinChoiceListBean> getWeixinChioceList(int page,int pageSize,String dttype,String key);
    }

    interface IWeixinView extends BaseTabsContract.IBaseTabsView<WeixinChoiceItemBean>{}

}
