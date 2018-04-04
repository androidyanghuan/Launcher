package cn.sn.zwcx.mvvm.bean.gankio;

import java.util.List;

/**
 * Created by on 2018/3/27 17:41.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class GankIoCustomListBean {

    private boolean error;

    private List<GankIoItemBean> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<GankIoItemBean> getResults() {
        return results;
    }

    public void setResults(List<GankIoItemBean> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "GankIoCustomListBean{" +
                "error=" + error +
                "results=" + results +
                '}';
    }
}
