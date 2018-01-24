package cn.sn.zwcx.yizi.contract.home.tabs;

import cn.sn.zwcx.yizi.contract.gankio.tabs.BaseTabsContract;
import cn.sn.zwcx.yizi.model.bean.zhihu.ZhihuDailyItemBean;
import cn.sn.zwcx.yizi.model.bean.zhihu.ZhihuDailyListBean;
import io.reactivex.Observable;

/**
 * Created by on 2018/1/10 8:25.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public interface ZhihuContract {
    abstract class ZhihuPresenter extends BaseTabsContract.BaseTabsPresenter<IZhihuModel,IZhihuView,ZhihuDailyItemBean>{

    }

    interface IZhihuView extends BaseTabsContract.IBaseTabsView<ZhihuDailyItemBean>{}

    interface IZhihuModel extends BaseTabsContract.IBaseTabsModel{

        /**
         * 获取日报list
         * @return
         */
        Observable<ZhihuDailyListBean> getDailyList();

        /**
         * 根据日期获取日报list------->20180110
         * @param date
         * @return
         */
        Observable<ZhihuDailyListBean> getDailyList(String date);
    }

}
