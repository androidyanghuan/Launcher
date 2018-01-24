package cn.sn.zwcx.yizi.contract.gankio;

import cn.sn.zwcx.sdk.base.BasePresenter;
import cn.sn.zwcx.sdk.base.IBaseFragment;
import cn.sn.zwcx.sdk.base.IBaseModel;
import cn.sn.zwcx.sdk.base.IBaseView;

/**
 * Created by on 2018/1/22 17:17.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public interface GankIoMainContract {
    // 主页接口
    abstract class GankIoMainPresenter extends BasePresenter<IGankIoMainModel,IGankIoMainView>{
        public abstract void getTabList();
    }

    interface IGankIoMainModel extends IBaseModel{
        String[] getTabs();
    }

    interface IGankIoMainView extends IBaseFragment{
        void showTabList(String[] tabs);
    }

}
