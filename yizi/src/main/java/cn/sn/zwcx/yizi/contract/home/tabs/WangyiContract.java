package cn.sn.zwcx.yizi.contract.home.tabs;

import cn.sn.zwcx.yizi.contract.gankio.tabs.BaseTabsContract;
import cn.sn.zwcx.yizi.model.bean.wangyi.WangyiNewsItemBean;
import cn.sn.zwcx.yizi.model.bean.wangyi.WangyiNewsListBean;
import io.reactivex.Observable;

/**
 * Created by on 2018/1/10 20:10.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 * 网易新闻接口
 */

public interface WangyiContract {
    abstract class WangyiPresenter extends BaseTabsContract.BaseTabsPresenter<IWangyiModel,IWangyiView,WangyiNewsItemBean>{

    }

    interface IWangyiModel extends BaseTabsContract.IBaseTabsModel{
        /**
         * 获取网易新闻list
         * @param id id
         * @return Observable
         */
        Observable<WangyiNewsListBean> getNewsList(int id);
    }

    interface IWangyiView extends BaseTabsContract.IBaseTabsView<WangyiNewsItemBean>{}

}
