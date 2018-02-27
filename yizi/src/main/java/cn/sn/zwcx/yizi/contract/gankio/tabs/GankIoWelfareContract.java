package cn.sn.zwcx.yizi.contract.gankio.tabs;

import cn.sn.zwcx.yizi.model.bean.gankio.GankIoWelfareItemBean;
import cn.sn.zwcx.yizi.model.bean.gankio.GankIoWelfareListBean;
import io.reactivex.Observable;

/**
 * Created by on 2018/1/27 16:54.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public interface GankIoWelfareContract {

    abstract class GankIoWelfarePresenter extends BaseTabsContract.BaseTabsPresenter<IGankIoWelfareModel,IGankIoWelfareView,GankIoWelfareItemBean>{

    }

    interface IGankIoWelfareModel extends BaseTabsContract.IBaseTabsModel{
        /**
         * 获取福利list
         * @param pre_page 每页条数
         * @param page     当前页
         * @return Observable
         */
        Observable<GankIoWelfareListBean> getWelfareList(int pre_page, int page);
    }

    interface IGankIoWelfareView extends BaseTabsContract.IBaseTabsView<GankIoWelfareItemBean>{

    }

}
