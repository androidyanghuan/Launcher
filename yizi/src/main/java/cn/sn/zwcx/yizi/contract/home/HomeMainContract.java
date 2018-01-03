package cn.sn.zwcx.yizi.contract.home;

import cn.sn.zwcx.sdk.base.BasePresenter;
import cn.sn.zwcx.sdk.base.IBaseFragment;
import cn.sn.zwcx.sdk.base.IBaseModel;

/**
 * Created by on 2017/12/27 11:51.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public interface HomeMainContract {
    // 主页接口
    abstract class HomeMainPresenter extends BasePresenter<IHomeMainModel,IHomeMainView>{
        public abstract void getTabList();
    }

    interface IHomeMainModel extends IBaseModel{
        String[] getTabs();
    }

    interface IHomeMainView extends IBaseFragment{
        void showTabList(String[] tabs);
    }
}
