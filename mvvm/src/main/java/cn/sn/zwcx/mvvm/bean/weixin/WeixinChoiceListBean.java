package cn.sn.zwcx.mvvm.bean.weixin;


import java.util.List;


/**
 * Created by on 2018/3/23 16:14.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class WeixinChoiceListBean {
    private String reason,error_code;

    private Result result;

    public WeixinChoiceListBean() {

    }

    public WeixinChoiceListBean(String reason, String error_code, Result result) {
        this.reason = reason;
        this.error_code = error_code;
        this.result = result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "WeixinChoiceListBean{" +
                "reason='" + reason + '\'' +
                ", error_code='" + error_code + '\'' +
                ", result=" + result +
                '}';
    }

    public class Result {
        private List<WeixinChoiceItemBean> list;
        private String totalPage,ps,pno;

        public Result() {
        }

        public Result(List<WeixinChoiceItemBean> list, String totalPage, String ps, String pno) {
            this.list = list;
            this.totalPage = totalPage;
            this.ps = ps;
            this.pno = pno;
        }

        public List<WeixinChoiceItemBean> getList() {
            return list;
        }

        public void setList(List<WeixinChoiceItemBean> list) {
            this.list = list;
        }

        public String getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(String totalPage) {
            this.totalPage = totalPage;
        }

        public String getPs() {
            return ps;
        }

        public void setPs(String ps) {
            this.ps = ps;
        }

        public String getPno() {
            return pno;
        }

        public void setPno(String pno) {
            this.pno = pno;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "list=" + list +
                    ", totalPage='" + totalPage + '\'' +
                    ", ps='" + ps + '\'' +
                    ", pno='" + pno + '\'' +
                    '}';
        }
    }
}
